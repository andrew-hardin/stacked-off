package org.tools4j.stacked.index

import org.apache.lucene.index.Term
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PostIndexTest {
    private lateinit var postIndex: PostIndex
    private val s1 = CoffeeSiteAssertions()

    @BeforeEach
    fun setup() {
        postIndex = createAndLoadPostIndex()
    }

    @Test
    fun testQueryAllPostsFromIndex() {
        val results = postIndex.search("coffee")
        assertThat(results).hasSize(3)
        s1.assertHasRawPost1(results);
        s1.assertHasRawPost2(results);
        s1.assertHasRawPost3(results);
    }

    @Test
    fun testGetByParentPostId() {
        val results = postIndex.getByParentUid("${s1.indexedSiteId}.1")
        assertThat(results).hasSize(1)
        s1.assertHasRawPost3(results);
    }

    @Test
    fun testGetByParentPostId_noPosts() {
        val results = postIndex.getByParentUid("${s1.indexedSiteId}.2")
        assertThat(results).isEmpty()
    }

    @Test
    fun testPurgeBySiteId() {
        assertThat(postIndex.searchByTerm(Term("indexedSiteId", s1.indexedSiteId))).hasSize(3)
        postIndex.purgeSite(s1.indexedSiteId)
        assertThat(postIndex.searchByTerm(Term("indexedSiteId", s1.indexedSiteId))).isEmpty()
    }
}