/*
 * Copyright 2026 Ramon Bouckaert
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.stefan_oltmann.kim.format.bmff.box

import de.stefan_oltmann.kim.common.MetadataOffset
import de.stefan_oltmann.kim.common.MetadataType
import de.stefan_oltmann.kim.common.toHex
import de.stefan_oltmann.kim.format.bmff.BMFFConstants
import de.stefan_oltmann.kim.format.bmff.BoxType

/**
 * EIC/ISO 14496-12 meta box
 *
 * The Meta Box is a container for several metadata boxes. This class represents a top-level Meta
 * Box that is not a sub-box of some other box.
 */
public class MetaBoxTopLevel(
    offset: Long,
    size: Long,
    largeSize: Long?,
    payload: ByteArray
) : MetaBox(offset, size, largeSize, payload), BoxContainer {

    /* Mandatory boxes in top-level META */
    public val primaryItemBox: PrimaryItemBox = boxes.find { it.type == BoxType.PITM } as PrimaryItemBox
    public val itemInfoBox: ItemInformationBox = boxes.find { it.type == BoxType.IINF } as ItemInformationBox
    public val itemLocationBox: ItemLocationBox = boxes.find { it.type == BoxType.ILOC } as ItemLocationBox

    public val referencesXmp: Boolean get() {
        for (extent in itemLocationBox.extents) {
            val itemInfo = itemInfoBox.map.get(extent.itemId) ?: continue
            if (itemInfo.itemType == BMFFConstants.ITEM_TYPE_MIME) return true
        }
        return false
    }

    public fun findMetadataOffsets(): List<MetadataOffset> {

        val offsets = mutableListOf<MetadataOffset>()

        for (extent in itemLocationBox.extents) {

            val itemInfo = itemInfoBox.map.get(extent.itemId) ?: continue

            when (itemInfo.itemType) {

                BMFFConstants.ITEM_TYPE_EXIF ->
                    offsets.add(
                        MetadataOffset(
                            type = MetadataType.EXIF,
                            offset = extent.offset,
                            length = extent.length
                        )
                    )

                BMFFConstants.ITEM_TYPE_MIME ->
                    offsets.add(
                        MetadataOffset(
                            type = MetadataType.XMP,
                            offset = extent.offset,
                            length = extent.length
                        )
                    )
            }
        }

        /* Sorted for safety. */
        offsets.sortBy { it.offset }

        return offsets
    }

    override fun toString(): String =
        "$type Box version=$version flags=${flags.toHex()} boxes=${boxes.map { it.type }}"
}
