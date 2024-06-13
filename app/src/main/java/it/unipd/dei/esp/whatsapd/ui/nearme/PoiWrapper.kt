package it.unipd.dei.esp.whatsapd.ui.nearme

import it.unipd.dei.esp.whatsapd.repository.database.Poi

/**
 * [PoiWrapper] represents the pair [Poi]-distance from a given position.
 * The class inherits all the properties from [Poi] and has an additional property [distance].
 * A [Poi] is passed as parameter (but it is not a property) to make the creation easier.
 */

class PoiWrapper(
	poi: Poi, val distance: Int
) : Poi(
	poi.name,
	poi.latitude,
	poi.longitude,
	poi.favourite,
	poi.wheelchairAccessible,
	poi.deafAccessible,
	poi.blindAccessible,
	poi.photoId,
	poi.photoAltText,
	poi.description
)
