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
package de.stefan_oltmann.kim.ktor

import de.stefan_oltmann.kim.Kim
import de.stefan_oltmann.kim.common.ImageReadException
import de.stefan_oltmann.kim.format.ImageMetadata
import de.stefan_oltmann.kim.input.KtorByteReadChannelByteReader
import de.stefan_oltmann.kim.input.KtorInputByteReader
import io.ktor.utils.io.ByteReadChannel
import kotlinx.io.Source
import kotlin.jvm.JvmStatic

/**
 * Extra object to have a nicer API for Java projects
 */
public object KimKtor {

    @JvmStatic
    @Throws(ImageReadException::class)
    public fun readMetadata(source: Source): ImageMetadata? =
        Kim.readMetadata(KtorInputByteReader(source))

    @JvmStatic
    @Throws(ImageReadException::class)
    public fun readMetadata(byteReadChannel: ByteReadChannel, contentLength: Long): ImageMetadata? =
        Kim.readMetadata(KtorByteReadChannelByteReader(byteReadChannel, contentLength))
}

@Throws(ImageReadException::class)
public fun Kim.readMetadata(source: Source): ImageMetadata? =
    KimKtor.readMetadata(source)

@Throws(ImageReadException::class)
public fun Kim.readMetadata(byteReadChannel: ByteReadChannel, contentLength: Long): ImageMetadata? =
    KimKtor.readMetadata(byteReadChannel, contentLength)
