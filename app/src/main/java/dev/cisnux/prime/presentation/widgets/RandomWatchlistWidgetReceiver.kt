package dev.cisnux.prime.presentation.widgets

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class RandomWatchlistWidgetReceiver(
    override val glanceAppWidget: GlanceAppWidget = RandomWatchlistWidget()
) : GlanceAppWidgetReceiver()