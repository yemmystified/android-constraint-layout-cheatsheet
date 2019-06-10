/*
 * Copyright (c) 2019 Hossain Khan
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

package com.hossainkhan.android.demo.ui.browse

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.hossainkhan.android.demo.R
import com.hossainkhan.android.demo.data.AppDataStore
import com.hossainkhan.android.demo.data.LayoutDataStore
import com.hossainkhan.android.demo.ui.layoutpreview.LayoutChainStyleActivity
import com.hossainkhan.android.demo.ui.layoutpreview.LayoutGuidelineBarrierActivity
import com.hossainkhan.android.demo.ui.layoutpreview.LayoutGuidelineGroupActivity
import com.hossainkhan.android.demo.ui.layoutpreview.LayoutVisibilityGoneActivity
import com.hossainkhan.android.demo.ui.resource.LearningResourceActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class LayoutBrowseViewModelTest {

    /**
     * Uses rule to test LiveData
     *
     * References:
     *  - https://medium.com/pxhouse/unit-testing-with-mutablelivedata-22b3283a7819
     *  - https://stackoverflow.com/questions/29945087/kotlin-and-new-activitytestrule-the-rule-must-be-public
     */
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    // https://imnotyourson.com/mockito-mocking-parameterized-class-in-kotlin/
    inline fun <reified T : Any> mockTyped() = Mockito.mock(T::class.java)

    private val resources: Resources = mock(Resources::class.java)
    private val preferences: SharedPreferences = mock(SharedPreferences::class.java)
    private val editor = mock(SharedPreferences.Editor::class.java)
    private var observer: Observer<BrowseResult<*>> = mockTyped()
    val resultCaptor = ArgumentCaptor.forClass(BrowseResult::class.java)
    private val layoutStore = LayoutDataStore(resources)
    private lateinit var owner: TestLifecycleOwner

    lateinit var sut: LayoutBrowseViewModel


    @Before
    fun setup() {
        `when`(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor)
        `when`(preferences.edit()).thenReturn(editor)
        owner = TestLifecycleOwner()
        sut = LayoutBrowseViewModel(AppDataStore(preferences, layoutStore))
    }


    @Test
    fun onLayoutItemSelected_givenGenericLayout_loadsDefaultPreview() {
        owner.start()
        sut.browseResult.observe(owner, observer)

        val layoutResId = 123
        sut.onLayoutItemSelected(layoutResId)
        verify(observer).onChanged(resultCaptor.capture())
        assertEquals(layoutResId, resultCaptor.value.layoutResId)
    }

    @Test
    fun onLayoutItemSelected_givenVisibilityGoneLayout_loadsVisibilityGonePreview() {
        owner.start()
        sut.browseResult.observe(owner, observer)

        val layoutResId = R.layout.preview_visibility_gone
        sut.onLayoutItemSelected(layoutResId)

        verify(observer).onChanged(resultCaptor.capture())
        assertEquals(LayoutVisibilityGoneActivity::class.java, resultCaptor.value.clazz)
        assertEquals(layoutResId, resultCaptor.value.layoutResId)
    }

    @Test
    fun onLayoutItemSelected_givenChainStyleLayout_loadsChainStylePreview() {
        owner.start()
        sut.browseResult.observe(owner, observer)

        val layoutResId = R.layout.preview_chain_style_main
        sut.onLayoutItemSelected(layoutResId)

        verify(observer).onChanged(resultCaptor.capture())
        assertEquals(LayoutChainStyleActivity::class.java, resultCaptor.value.clazz)
        assertEquals(layoutResId, resultCaptor.value.layoutResId)
    }

    @Test
    fun onLayoutItemSelected_givenGuidelineBarrierLayout_loadsGuidelineBarrierPreview() {
        owner.start()
        sut.browseResult.observe(owner, observer)

        val layoutResId = R.layout.preview_virtual_helper_barrier
        sut.onLayoutItemSelected(layoutResId)

        verify(observer).onChanged(resultCaptor.capture())
        assertEquals(LayoutGuidelineBarrierActivity::class.java, resultCaptor.value.clazz)
        assertEquals(layoutResId, resultCaptor.value.layoutResId)
    }

    @Test
    fun onLayoutItemSelected_givenGuidelineGroupLayout_loadsGuidelineGroupPreview() {
        owner.start()
        sut.browseResult.observe(owner, observer)

        val layoutResId = R.layout.preview_virtual_helper_group
        sut.onLayoutItemSelected(layoutResId)

        verify(observer).onChanged(resultCaptor.capture())
        assertEquals(LayoutGuidelineGroupActivity::class.java, resultCaptor.value.clazz)
        assertEquals(layoutResId, resultCaptor.value.layoutResId)
    }

    @Test
    fun onLayoutItemSelected_givenLearningResource_loadsLearningResource() {
        owner.start()
        sut.browseResult.observe(owner, observer)

        val layoutResId = R.layout.activity_learning_resource
        sut.onLayoutItemSelected(layoutResId)

        verify(observer).onChanged(resultCaptor.capture())
        assertEquals(LearningResourceActivity::class.java, resultCaptor.value.clazz)
        assertEquals(null, resultCaptor.value.layoutResId)
    }
}