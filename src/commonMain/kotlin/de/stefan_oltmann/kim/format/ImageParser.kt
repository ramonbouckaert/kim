/*
 * Copyright 2025 Ashampoo GmbH & Co. KG
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
package de.stefan_oltmann.kim.format

import de.stefan_oltmann.kim.common.ImageReadException
import de.stefan_oltmann.kim.format.bmff.BaseMediaFileFormatImageParser
import de.stefan_oltmann.kim.format.gif.GifImageParser
import de.stefan_oltmann.kim.format.jpeg.JpegImageParser
import de.stefan_oltmann.kim.format.png.PngImageParser
import de.stefan_oltmann.kim.format.raf.RafImageParser
import de.stefan_oltmann.kim.format.tiff.TiffImageParser
import de.stefan_oltmann.kim.format.webp.WebPImageParser
import de.stefan_oltmann.kim.input.ByteReader
import de.stefan_oltmann.kim.model.ImageFormat
import kotlin.jvm.JvmStatic

public fun interface ImageParser {

    @Throws(ImageReadException::class)
    public fun parseMetadata(byteReader: ByteReader): ImageMetadata

    public companion object {

        @JvmStatic
        public fun forFormat(imageFormat: ImageFormat): ImageParser? =
            when (imageFormat) {

                ImageFormat.JPEG -> JpegImageParser

                ImageFormat.PNG -> PngImageParser

                ImageFormat.WEBP -> WebPImageParser

                ImageFormat.TIFF,
                ImageFormat.CR2,
                ImageFormat.NEF,
                ImageFormat.ARW,
                ImageFormat.RW2,
                ImageFormat.ORF -> TiffImageParser

                ImageFormat.RAF -> RafImageParser

                ImageFormat.HEIC,
                ImageFormat.AVIF,
                ImageFormat.CR3,
                ImageFormat.JXL -> BaseMediaFileFormatImageParser

                ImageFormat.GIF -> GifImageParser

                else -> null
            }
    }
}
