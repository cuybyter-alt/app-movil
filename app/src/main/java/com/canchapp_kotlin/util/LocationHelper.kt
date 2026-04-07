package com.canchapp_kotlin.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Requests the device's current location once.
 * Requires ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION to be granted before calling.
 */
@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): Location? =
    suspendCancellableCoroutine { cont ->
        val client = LocationServices.getFusedLocationProviderClient(context)
        val cts = CancellationTokenSource()

        cont.invokeOnCancellation { cts.cancel() }

        client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
            .addOnSuccessListener { location -> cont.resume(location) }
            .addOnFailureListener { cont.resume(null) }
    }
