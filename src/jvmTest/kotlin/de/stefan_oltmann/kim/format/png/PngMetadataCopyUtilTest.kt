/*
 * Copyright 2026 Stefan Oltmann
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
package de.stefan_oltmann.kim.format.png

import com.goncalossilva.resources.Resource
import de.stefan_oltmann.kim.common.copyTo
import de.stefan_oltmann.kim.common.exists
import de.stefan_oltmann.kim.common.readBytes
import de.stefan_oltmann.kim.testdata.KimTestData
import kotlinx.io.files.Path
import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class PngMetadataCopyUtilTest {

    fun getFullImageDiskPath(index: Int): String =
        Resource("src/commonTest/resources/de/stefan_oltmann/kim/testdata/full/${KimTestData.getFileName(index)}").path

    @Test
    fun testCopy() {

        val source = Path(getFullImageDiskPath(52))

        val destination = Path("build/copy_test.png")

        /* Copy test image to local folder. */
        Path(getFullImageDiskPath(51)).copyTo(destination)

        /* Check that the file was actually copied. */
        assertTrue(destination.exists(), "copy_test.png does not exist.")

        PngMetadataCopyUtil.copy(
            source = source,
            destination = destination
        )

        val expectedBytes =
            Path("src/commonTest/resources/de/stefan_oltmann/kim/copy_test.png")
                .readBytes()

        val actualBytes = destination.readBytes()

        val equals = expectedBytes.contentEquals(actualBytes)

        if (!equals)
            fail("copy_test.png has not the expected bytes!")
    }

    @Test
    fun testCopyByteArray() {

        val sourceBytes = Path(getFullImageDiskPath(52)).readBytes()

        val destinationBytes = Path(getFullImageDiskPath(51)).readBytes()

        val expectedBytes =
            Path("src/commonTest/resources/de/stefan_oltmann/kim/copy_test.png")
                .readBytes()

        val actualBytes = PngMetadataCopyUtil.copy(
            source = sourceBytes,
            destination = destinationBytes
        )

        val equals = expectedBytes.contentEquals(actualBytes)

        if (!equals)
            fail("copy_test.png has not the expected bytes!")
    }
}
