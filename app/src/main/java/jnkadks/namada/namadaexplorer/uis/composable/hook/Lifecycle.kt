package jnkadks.namada.namadaexplorer.uis.composable.hook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle

@Composable
fun OnResume(lifecycleState: Lifecycle.State, handler: () -> Unit) {
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) {
            handler()
        }
    }
}

@Composable
fun OnStarted(lifecycleState: Lifecycle.State, handler: () -> Unit) {
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.STARTED) {
            handler()
        }
    }
}

@Composable
fun OnDestroyed(lifecycleState: Lifecycle.State, handler: () -> Unit) {
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.DESTROYED) {
            handler()
        }
    }
}