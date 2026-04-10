# Guía CanchApp — Kotlin para humanos

Esta guía explica cómo funciona la app desde cero. No asume conocimiento previo de Android.

---

## 1. Cómo viaja un dato del servidor a la pantalla

Cuando abrís la home y ves la lista de complejos, pasan estas cosas en orden:

```
Backend (internet)
    ↓  HTTP GET /api/complexes/
RetrofitClient        ← configura el cliente HTTP
    ↓
ApiService            ← define qué endpoints existen
    ↓
ComplexRepository     ← llama al endpoint y maneja errores
    ↓
ComplexViewModel      ← guarda el resultado en un StateFlow
    ↓
HomeScreen            ← observa el StateFlow y dibuja la UI
```

### 1.1 RetrofitClient — el cliente HTTP

**Archivo:** `data/network/RetrofitClient.kt`

```kotlin
object RetrofitClient {                        // "object" = singleton, solo existe una instancia
    private const val BASE_URL = "https://canchapp-backend.onrender.com/"

    val api: ApiService by lazy {              // "by lazy" = se crea SOLO cuando alguien lo pide por primera vez
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)              // OkHttp maneja los timeouts y logs
            .addConverterFactory(GsonConverterFactory.create())  // convierte JSON → objetos Kotlin
            .build()
            .create(ApiService::class.java)    // genera automáticamente la implementación de la interfaz
    }
}
```

**Por qué `object`?** En Kotlin `object` crea una clase que tiene exactamente una instancia en toda la app.
No podés hacer `RetrofitClient()` — siempre accedés con `RetrofitClient.api`.

### 1.2 ApiService — el mapa de endpoints

**Archivo:** `data/network/ApiService.kt`

```kotlin
interface ApiService {
    @GET("api/complexes/")
    suspend fun getComplexes(
        @Query("page") page: Int = 1       // agrega ?page=1 a la URL
    ): Response<ComplexListResponse>       // Response<T> incluye el código HTTP (200, 404, etc.)
}
```

`suspend` significa que la función es **coroutine-friendly**: puede pausarse mientras espera la red
sin bloquear el hilo principal (la pantalla nunca se congela).

### 1.3 Resource — cómo representar "está cargando / falló / llegó"

**Archivo:** `util/Resource.kt`

```kotlin
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()   // llegó bien, data tiene los datos
    data class Error(val message: String) : Resource<Nothing>()  // falló, message explica por qué
    object Loading : Resource<Nothing>()                  // en progreso, no hay datos aún
}
```

`sealed class` es como un enum pero cada variante puede tener datos distintos.
En la UI usás `when` para manejar cada caso:

```kotlin
when (val state = complexesState) {
    is Resource.Loading -> { /* mostrar spinner */ }
    is Resource.Success -> { /* usar state.data */ }
    is Resource.Error   -> { /* mostrar state.message */ }
    null -> { /* no se pidió nada todavía */ }
}
```

### 1.4 ComplexRepository — lógica de negocio

**Archivo:** `data/repository/ComplexRepository.kt`

```kotlin
suspend fun getComplexes(page: Int = 1, lat: Double? = null, lon: Double? = null)
        : Resource<ComplexListResponse> {
    return try {
        val response = api.getComplexes(page, lat, lon)   // llama a la red
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true) Resource.Success(body)  // ✅ OK
            else Resource.Error(body?.message ?: "Error")       // ✅ HTTP 200 pero el backend dijo error
        } else {
            Resource.Error("Error ${response.code()}")          // ❌ HTTP 4xx / 5xx
        }
    } catch (e: IOException) {
        Resource.Error("Sin conexión a internet")               // ❌ sin red
    }
}
```

El Repository **nunca dibuja nada** — solo habla con la red y devuelve un `Resource`.

### 1.5 ComplexViewModel — el puente entre datos y UI

**Archivo:** `ui/complex/ComplexViewModel.kt`

```kotlin
class ComplexViewModel : ViewModel() {         // ViewModel sobrevive a rotaciones de pantalla
    private val _complexesState = MutableStateFlow<Resource<ComplexListResponse>?>(null)
    val complexesState = _complexesState.asStateFlow()  // la UI solo puede LEER, no escribir

    init {
        loadComplexes()    // se llama automáticamente cuando se crea el ViewModel
    }

    fun loadComplexes(page: Int = 1) {
        viewModelScope.launch {                // lanza una coroutine en el scope del ViewModel
            _complexesState.value = Resource.Loading  // primero Loading
            _complexesState.value = repository.getComplexes(page, userLat, userLon)  // luego Success o Error
        }
    }
}
```

**StateFlow** es como una variable reactiva: cada vez que cambia su valor, todos los que la "observan"
se enteran y la UI se redibuja sola.

---

## 2. Cómo funciona la UI — Jetpack Compose

En Android tradicional se usaban archivos XML para diseñar pantallas. Con Compose,
la UI es **código Kotlin puro**.

### 2.1 Composable — el bloque básico

```kotlin
@Composable                          // esta anotación le dice a Compose que es una función de UI
fun ComplexCard(complex: ComplexDto) {
    Card {
        Text(text = complex.name)    // cada vez que complex.name cambia, solo este Text se redibuja
    }
}
```

Las funciones `@Composable` **no retornan nada** (Unit) — describen cómo se ve algo.
Compose las llama cuando necesita dibujar o actualizar la pantalla.

### 2.2 State — qué hace que la pantalla cambie

```kotlin
var searchQuery by remember { mutableStateOf("") }     // recuerda el valor entre recomposiciones
```

- `mutableStateOf("")` crea una variable observable.
- `remember { }` hace que no se resetee cada vez que la función se redibuja.
- `by` permite usar `searchQuery` directamente en lugar de `searchQuery.value`.

Cada vez que `searchQuery` cambia, Compose redibuja solo las partes que lo usan.

### 2.3 collectAsState — conectar StateFlow con la UI

```kotlin
val complexesState by complexViewModel.complexesState.collectAsState()
```

Esto convierte el `StateFlow` del ViewModel en un State de Compose.
Cada vez que el ViewModel actualiza `complexesState`, el `HomeScreen` se redibuja.

---

## 3. Navegación en CanchApp — estado como enum

Esta app no usa `NavController` (el sistema de navegación "oficial" de Android).
En cambio, usa un **enum + `when`** en `MainActivity`. Es más simple y fácil de entender.

**Archivo:** `MainActivity.kt`

```kotlin
enum class AppScreen { REGISTER_LANDING, LOGIN, REGISTER_FORM, HOME }

// En el setContent:
var currentScreen by remember { mutableStateOf(AppScreen.REGISTER_LANDING) }

when (currentScreen) {
    AppScreen.LOGIN -> LoginScreen(
        onLoginSuccess  = { currentScreen = AppScreen.HOME },    // cambiar pantalla = cambiar el enum
        onRegisterClick = { currentScreen = AppScreen.REGISTER_LANDING }
    )
    AppScreen.HOME -> HomeScreen(
        onLogout = {
            authViewModel.logout()
            currentScreen = AppScreen.LOGIN
        }
    )
    // ...
}
```

**Cómo funciona:** `currentScreen` es un State. Cuando cambia (`currentScreen = AppScreen.HOME`),
Compose redibuja el `when` y muestra la pantalla nueva. El "Back" no está implementado — es intencional.

**Comparado con NavController:**
| Enum state machine | NavController |
|---|---|
| Simple, 10 líneas | Más complejo, más poderoso |
| Sin pila de historial | Pila de historial automática |
| Ideal para apps simples | Ideal para apps con muchas pantallas |

---

## 4. HomeScreen — cómo funciona en detalle

### 4.1 El drawer (menú hamburguesa)

```kotlin
val drawerState = rememberDrawerState(DrawerValue.Closed)   // estado: abierto o cerrado
val scope       = rememberCoroutineScope()                   // para lanzar coroutines en la UI

ModalNavigationDrawer(
    drawerState     = drawerState,
    gesturesEnabled = !showMap,   // deshabilitado en el mapa para no interferir con el scroll
    drawerContent   = { AppDrawerContent(...) }
) {
    // contenido principal de la pantalla
    TopNavBar(
        onMenuClick = { scope.launch { drawerState.open() } }  // abrir con animación (coroutine)
    )
}
```

`drawerState.open()` es `suspend` — necesita una coroutine para ejecutarse. Por eso `scope.launch { }`.

### 4.2 Permisos de ubicación

```kotlin
val locationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->                    // este bloque se ejecuta cuando el usuario responde el diálogo
    val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || ...
    if (granted) {
        scope.launch {
            val location = getCurrentLocation(context)   // función suspend — espera la ubicación
            userLat = location?.latitude
            complexViewModel.updateLocation(...)         // recarga la lista con distancias reales
        }
    }
}

LaunchedEffect(Unit) {    // se ejecuta UNA SOLA VEZ cuando la pantalla aparece
    if (/* ya tiene permiso */) {
        // obtener ubicación directamente
    } else {
        locationPermissionLauncher.launch(arrayOf(...))  // mostrar diálogo de permiso
    }
}
```

### 4.3 Lista filtrada

```kotlin
val allItems = state.data.data?.items ?: emptyList()       // ?: es "si es null, usar esto"
val filtered = if (searchQuery.isBlank()) allItems
               else allItems.filter {                       // filter = quedarme con los que cumplen
                   it.name.contains(searchQuery.trim(), ignoreCase = true)
               }
```

Este filtrado es **local** — no hace una nueva petición al servidor,
solo filtra la lista que ya está en memoria.

---

## 5. Cómo funciona el mapa de Mapbox

### 5.1 Token de acceso

Mapbox requiere un token público para renderizar los tiles del mapa.
Está en `app/src/main/res/values/mapbox_access_token.xml` (en `.gitignore` para no subirse a Git).
El SDK lo lee automáticamente por el nombre del string (`mapbox_access_token`).

### 5.2 MapboxMap + MapEffect

```kotlin
MapboxMap(
    mapViewportState = mapViewportState,         // controla la cámara (posición, zoom)
    style = { MapStyle(style = Style.DARK) }     // estilo visual del mapa
) {
    MapEffect(complexes, userLat, userLon) { mapView ->  // bloque imperativo — se re-ejecuta
        // cuando cambia cualquiera de los parámetros anteriores (complexes, userLat, userLon)

        mapView.viewAnnotationManager.removeAllViewAnnotations()  // limpiar burbujas anteriores

        complexes.forEach { complex ->
            val view = mapView.viewAnnotationManager.addViewAnnotation(
                resId = R.layout.marker_complex,   // layout XML de la burbuja verde
                options = viewAnnotationOptions {
                    geometry(Point.fromLngLat(complex.longitude, complex.latitude))
                }
            )
            view.findViewById<TextView>(R.id.tvComplexName).text = complex.name
            view.setOnClickListener { selectedComplex = complex }   // tap → mostrar card
        }
    }
}
```

`MapEffect` es el "escape hatch" de Compose hacia el API imperativo de Mapbox.
Dentro de ese bloque ya no estás en Compose — estás trabajando directamente con el `MapView` nativo.

### 5.3 Viewport (cámara)

```kotlin
val mapViewportState = rememberMapViewportState {
    setCameraOptions {
        center(Point.fromLngLat(centerLon, centerLat))  // longitud va primero (ojo!)
        zoom(12.0)   // 1 = mundo entero, 20 = edificio individual
    }
}

// Mover la cámara con animación:
mapViewportState.flyTo(
    CameraOptions.Builder()
        .center(Point.fromLngLat(lon, lat))
        .zoom(12.0)
        .build()
)
```

### 5.4 ViewAnnotation vs PointAnnotation

| ViewAnnotation (el que usamos) | PointAnnotation |
|---|---|
| Vista XML nativa anclada al mapa | Solo texto/icono simple |
| Podés poner cualquier layout | Más limitado visualmente |
| `mapView.viewAnnotationManager` | `mapView.annotations.createPointAnnotationManager()` |
| Soporta clicks con `setOnClickListener` | Necesita `addClickListener` |

---

## 6. Caché — qué persiste y qué no

### Lo que SÍ persiste mientras la app está abierta

- **ViewModel**: `ComplexViewModel` y `AuthViewModel` viven mientras la app esté en memoria.
  La lista de complejos no se re-descarga si volvés a HOME desde otra pantalla.

### Lo que NO persiste cuando cerrás la app

- El usuario logueado (`loggedUser` en `AuthViewModel`) se borra.
- La lista de complejos se borra.

**Consecuencia:** si cerrás la app y la volvés a abrir, tenés que loguearte de nuevo.

### Por qué no hay caché persistente (todavía)

Para tener caché que sobreviva al cierre de la app necesitarías:
- **SharedPreferences** — para guardar el token JWT (strings simples)
- **Room** — base de datos local para guardar la lista de complejos
- **DataStore** — alternativa moderna a SharedPreferences

Actualmente la app no implementa ninguno de estos — cada sesión empieza desde cero.

---

## 7. Flujo completo de una acción de usuario

**Ejemplo: el usuario abre la app y ve los complejos**

```
1. MainActivity.onCreate()
   └─ currentScreen = REGISTER_LANDING

2. Usuario toca "Iniciar sesión" → currentScreen = LOGIN

3. LoginScreen aparece
   └─ authViewModel.login(email, password)
       └─ viewModelScope.launch { }       ← coroutine en background
           └─ AuthRepository.login()
               └─ api.login()             ← HTTP POST (suspend, no bloquea UI)
                   └─ Resource.Success(LoginResponse)
       └─ _loggedUser.value = user        ← actualiza StateFlow

4. LoginScreen observa loginState
   └─ es Success → delay(1.2s) → onLoginSuccess() → currentScreen = HOME

5. HomeScreen se crea
   └─ ComplexViewModel.init { loadComplexes() }
       └─ _complexesState.value = Loading  ← UI muestra spinner
       └─ api.getComplexes()              ← HTTP GET
       └─ _complexesState.value = Success(data)  ← UI muestra la lista

6. HomeScreen observa complexesState
   └─ es Success → LazyColumn dibuja las ComplexCard
```

---

## 8. Glosario rápido

| Término | Qué es |
|---|---|
| `suspend fun` | Función que puede pausarse (esperar red/disco) sin bloquear la UI |
| `coroutine` | Tarea liviana que puede correr en background. `viewModelScope.launch { }` la inicia |
| `StateFlow` | Variable observable. Quien la "colecta" se redibuja cuando cambia |
| `@Composable` | Función que describe parte de la UI. Compose la llama cuando necesita dibujar |
| `remember { }` | Hace que un valor sobreviva entre recomposiciones de Compose |
| `ViewModel` | Sobrevive a rotaciones de pantalla. Guarda el estado de la UI |
| `Repository` | Capa que habla con la red/base de datos. No sabe nada de UI |
| `sealed class` | Tipo que tiene un número fijo de subtipos (usado en `Resource`) |
| `by lazy` | El valor se calcula solo la primera vez que se accede |
| `?: ` | Operador Elvis: `a ?: b` = "si a es null, usar b" |
| `?.` | Operador safe call: `a?.nombre` = "si a es null, devolver null en lugar de crashear" |
