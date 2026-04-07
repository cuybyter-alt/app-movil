# SKILL: Mapbox Maps SDK en Kotlin para Android
> Guía de referencia para agente — App de canchas deportivas  
> Fuente oficial: https://docs.mapbox.com/android/maps/guides/

---

## Contexto del proyecto

Esta guía aplica a una aplicación Android escrita en **Kotlin** que permite a usuarios ver y reservar canchas deportivas en un mapa interactivo. El mapa debe mostrar la ubicación de las canchas, permitir hacer clic sobre ellas y ver detalles.

---

## 1. Requisitos previos

| Requisito | Mínimo |
|---|---|
| Android SDK | 21 (Android 5.0) |
| Kotlin | 1.6.0 o superior |
| Java compatibility | Java 8 (`sourceCompatibility` / `targetCompatibility`) |
| OpenGL ES | 3 |
| Cuenta Mapbox | Obligatoria — [mapbox.com](https://account.mapbox.com/auth/signup/) |

---

## 2. Configurar credenciales

### 2.1 Token de acceso público

Crear el archivo `app/res/values/mapbox_access_token.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools">
    <string name="mapbox_access_token" translatable="false" tools:ignore="UnusedResources">
        TU_TOKEN_AQUI
    </string>
</resources>
```

> El SDK busca automáticamente el string `mapbox_access_token` al inicializar el mapa. No se necesita pasarlo manualmente.

### 2.2 Permisos de ubicación (AndroidManifest.xml)

Agregar arriba del tag `<application>`:

```xml
<!-- Ubicación aproximada (obligatoria si se usa cualquier nivel de ubicación) -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<!-- Ubicación precisa (opcional, para mostrar posición exacta del usuario) -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

---

## 3. Agregar la dependencia

### 3.1 Repositorio Maven de Mapbox (`settings.gradle.kts`)

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Repositorio de Mapbox — agregar aquí, NO dentro de pluginManagement
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
        }
    }
}
```

### 3.2 Dependencia SDK (`app/build.gradle.kts`)

```kotlin
dependencies {
    // SDK principal (NDK 27 con soporte páginas 16 KB)
    implementation("com.mapbox.maps:android-ndk27:11.20.2")

    // Si se usa Jetpack Compose:
    implementation("com.mapbox.extension:maps-compose-ndk27:11.20.2")
}
```

Si la app no requiere soporte de páginas de 16 KB, se puede usar:
```kotlin
implementation("com.mapbox.maps:android:11.20.2")
```

Para Jetpack Compose, además agregar en `android {}`:
```kotlin
android {
    buildFeatures {
        compose = true
    }
}
```

---

## 4. Mostrar el mapa

### 4.1 Con XML (Vista tradicional)

En el layout XML de la Activity:

```xml
<com.mapbox.maps.MapView
    android:id="@+id/mapView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

En la Activity (Kotlin):

```kotlin
class MapCanchasActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-76.5320, 2.4419)) // Popayán, Colombia
                .zoom(13.0)
                .build()
        )

        mapView.mapboxMap.loadStyle(Style.STANDARD)
    }
}
```

### 4.2 Con Jetpack Compose

```kotlin
@Composable
fun MapaCanchas() {
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        style = { MapStyle(style = Style.STANDARD) }
    )
}
```

---

## 5. Agregar marcadores de canchas (PointAnnotations)

Cada cancha se representa como un `PointAnnotation` con un ícono personalizado.

```kotlin
// Dentro del callback de loadStyle:
mapView.mapboxMap.loadStyle(Style.STANDARD) {

    val annotationPlugin = mapView.annotations
    val pointAnnotationManager = annotationPlugin.createPointAnnotationManager()

    // Convertir drawable a bitmap
    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_cancha)

    val canchas = listOf(
        Pair("Cancha El Parque", Point.fromLngLat(-76.5310, 2.4430)),
        Pair("Cancha Central", Point.fromLngLat(-76.5350, 2.4400)),
    )

    canchas.forEach { (nombre, punto) ->
        val options = PointAnnotationOptions()
            .withPoint(punto)
            .withIconImage(bitmap)
            .withTextField(nombre)
        pointAnnotationManager.create(options)
    }

    // Detectar clic sobre una cancha
    pointAnnotationManager.addClickListener { annotation ->
        val texto = annotation.textField ?: "Cancha"
        Toast.makeText(this@MapCanchasActivity, "Seleccionaste: $texto", Toast.LENGTH_SHORT).show()
        true
    }
}
```

---

## 6. Mover la cámara programáticamente

```kotlin
// Ir a una cancha con animación suave
mapView.mapboxMap.flyTo(
    CameraOptions.Builder()
        .center(Point.fromLngLat(-76.5310, 2.4430))
        .zoom(15.0)
        .build()
)

// O con setCamera() sin animación:
mapView.mapboxMap.setCamera(
    cameraOptions {
        center(Point.fromLngLat(-76.5310, 2.4430))
        zoom(15.0)
    }
)
```

---

## 7. Mostrar popup/detalle al tocar una cancha (ViewAnnotation)

Para mostrar una tarjeta con info de la cancha al hacer clic:

```kotlin
val viewAnnotationManager = mapView.viewAnnotationManager

// Crear la vista de detalle (puede ser un layout XML inflado)
val view = layoutInflater.inflate(R.layout.popup_cancha, null)
view.findViewById<TextView>(R.id.nombre).text = "Cancha El Parque"
view.findViewById<TextView>(R.id.precio).text = "$20.000/hora"

viewAnnotationManager.addViewAnnotation(
    view,
    viewAnnotationOptions {
        geometry(Point.fromLngLat(-76.5310, 2.4430))
        allowOverlap(false)
    }
)
```

---

## 8. Estilos de mapa disponibles

| Constante | Descripción |
|---|---|
| `Style.STANDARD` | Estilo moderno por defecto |
| `Style.SATELLITE_STREETS` | Vista satélite con calles |
| `Style.OUTDOORS` | Ideal para espacios abiertos/deportivos |
| `Style.LIGHT` | Mapa claro minimalista |
| `Style.DARK` | Mapa oscuro |

Para canchas deportivas se recomienda `Style.OUTDOORS` o `Style.STANDARD`.

---

## 9. Buenas prácticas

- **Inicializar el token antes de inflar la vista.** Si se configura en runtime, llamar `MapboxOptions.accessToken = "..."` antes de `setContentView()`.
- **No usar clases deprecadas.** Las clases `Marker`, `Polygon`, `Polyline` y métodos como `addMarker()` están deprecados desde SDK v7. Usar siempre el sistema de `AnnotationPlugin`.
- **Manejo del ciclo de vida.** El `MapView` maneja automáticamente su propio ciclo de vida en versiones recientes del SDK; no es necesario llamar `mapView.onStart()`, `mapView.onStop()`, etc. manualmente.
- **Evitar descargas innecesarias.** Configurar la posición inicial de la cámara en XML o en `MapboxMapOptions` para no cargar tiles fuera del área de interés.

---

## 10. Fuentes

| Recurso | URL |
|---|---|
| Guía de instalación oficial | https://docs.mapbox.com/android/maps/guides/install/ |
| Guía general del SDK | https://docs.mapbox.com/android/maps/guides/ |
| Ejemplos de PointAnnotations | https://docs.mapbox.com/android/maps/examples/add-point-annotations/ |
| Ejemplos de ViewAnnotations | https://docs.mapbox.com/android/maps/examples/view-annotations-basic-example/ |
| Referencia de API | https://docs.mapbox.com/android/maps/api-reference/ |
| Repositorio GitHub del SDK | https://github.com/mapbox/mapbox-maps-android |