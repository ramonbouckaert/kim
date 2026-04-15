/*
 * Copyright 2026 Stefan Oltmann
 * Copyright 2025 Ashampoo GmbH & Co. KG
 * Copyright 2002-2023 Drew Noakes and contributors
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

import de.stefan_oltmann.kim.common.ImageReadException
import de.stefan_oltmann.kim.format.bmff.BoxReader
import de.stefan_oltmann.kim.format.bmff.BoxType
import de.stefan_oltmann.kim.input.ByteArrayByteReader

/**
 * EIC/ISO 14496-12 movie box
 *
 * The Track Box is a container for several sub boxes.
 */
public class TrackBox(
    offset: Long,
    size: Long,
    largeSize: Long?,
    payload: ByteArray
) : Box(BoxType.TRAK, offset, size, largeSize, payload), BoxContainer {

    override val boxes: List<Box>

    public val trackHeaderBox: Box
    public val mediaBox: MediaBox

    init {

        val byteReader = ByteArrayByteReader(payload)

        boxes = BoxReader.readBoxes(
            byteReader = byteReader,
            stopAfterMetadataRead = false,
            positionOffset = 4,
            offsetShift = offset + 8
        )

        if (boxes.isEmpty())
            throw ImageReadException("Track box should contain boxes: $boxes")

        val localTrackHeaderBox = boxes.find { it.type == BoxType.TKHD }
        val localMediaBox = boxes.find { it.type == BoxType.MDIA }

        if (localTrackHeaderBox == null || localMediaBox == null)
            throw ImageReadException("Track box should contain 'tkhd' and 'mdia' boxes: $boxes")

        trackHeaderBox = localTrackHeaderBox
        mediaBox = localMediaBox as MediaBox
    }

    override fun toString(): String =
        "Box '$type' @$offset boxes=${boxes.map { it.type }}"
}
