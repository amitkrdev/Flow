package com.none.flow.presentation.dragdrop

import android.view.View
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.core.view.HapticFeedbackConstantsCompat

fun Modifier.dragContainer(view: View, dragDropState: DragDropState): Modifier {
    return pointerInput(dragDropState) {

        detectDragGesturesAfterLongPress(
            onDrag = { change, offset ->
                change.consume()
                dragDropState.onDrag(offset = offset)
            },
            onDragStart = { offset ->
                dragDropState.onDragStart(offset)
                view.performHapticFeedback(HapticFeedbackConstantsCompat.DRAG_START)
            },
            onDragEnd = {
                dragDropState.onDragInterrupted()
                view.performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM)
            },
            onDragCancel = { dragDropState.onDragInterrupted()
                view.performHapticFeedback(HapticFeedbackConstantsCompat.REJECT)
            }
        )
    }
}