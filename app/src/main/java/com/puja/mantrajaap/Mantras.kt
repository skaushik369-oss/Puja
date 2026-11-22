package com.puja.mantrajaap

/**
 * How many times a mantra is chanted when you press Play.
 * Change this one number to change it for every mantra.
 */
const val DEFAULT_REPEAT_COUNT = 108

/**
 * One item in the list.
 *
 * @param displayText what is shown on screen (English letters, as written in the original notes).
 * @param hindiSpeech the same text in Devanagari, which is what the Hindi voice reads aloud.
 *   Devanagari gives a much more accurate Hindi pronunciation than English spelling.
 * @param repeatCount how many times Play repeats it. Override per mantra if needed.
 */
data class Mantra(
    val number: Int,
    val title: String,
    val displayText: String,
    val hindiSpeech: String,
    val note: String? = null,
    val repeatCount: Int = DEFAULT_REPEAT_COUNT,
) {
    /**
     * The pieces sent to the speech engine for one full recitation, in order.
     * Short mantras are one piece. Long texts (the Chalisa) are spoken line by line,
     * which keeps each piece under the engine's length limit.
     */
    val speechParts: List<String> by lazy {
        val lines = hindiSpeech.lines()
            .map { it.replace(VERSE_NUMBER, "॥").trim() }
            .filter { it.isNotEmpty() && it !in HEADINGS }
        if (lines.joinToString(" ").length <= 400) listOf(lines.joinToString(" ")) else lines
    }

    private companion object {
        val VERSE_NUMBER = Regex("॥\\s*[०-९0-9]+\\s*॥")
        val HEADINGS = setOf("॥ दोहा ॥", "॥ चौपाई ॥")
    }
}

val MANTRAS: List<Mantra> = listOf(
    Mantra(
        number = 1,
        title = "Kamakhya Devi",
        displayText = "Kleem Kleem Kamakhyayai Namah.",
        hindiSpeech = "क्लीं क्लीं कामाख्यायै नमः",
    ),
    Mantra(
        number = 2,
        title = "Maha Mrityunjay Jap",
        displayText = """
            Om Tryambakam Yajamahe
            Sugandhim Pushti vardhanam
            Urvarukamiva Bandhanaan
            Mrityor mukshiya Maamritaat
        """.trimIndent(),
        hindiSpeech = """
            ॐ त्र्यम्बकं यजामहे सुगन्धिं पुष्टिवर्धनम् ।
            उर्वारुकमिव बन्धनान् मृत्योर्मुक्षीय मामृतात् ॥
        """.trimIndent(),
    ),
    Mantra(
        number = 3,
        title = "Vishnu Sahasranama",
        displayText = """
            Shrī Rāma Rāma Rāmeti Rame Rāme Manorame
            Sahasranāma tat tulyam Rāma nāma varānane
        """.trimIndent(),
        hindiSpeech = """
            श्री राम राम रामेति रमे रामे मनोरमे ।
            सहस्रनाम तत्तुल्यं रामनाम वरानने ॥
        """.trimIndent(),
    ),
    Mantra(
        number = 4,
        title = "Surya Beej Mantra",
        displayText = "Om Hraam Hreem Hraum Sah Suryaya Namah",
        hindiSpeech = "ॐ ह्रां ह्रीं ह्रौं सः सूर्याय नमः",
        // Traditionally chanted 21 times. To use 21 here, add: repeatCount = 21,
        note = "Traditionally chanted × 21",
    ),
    Mantra(
        number = 5,
        title = "Rahu Remedy",
        displayText = "Om Raam Rahave Namah",
        hindiSpeech = "ॐ रां राहवे नमः",
    ),
    Mantra(
        number = 6,
        title = "Shani Beej Mantra",
        displayText = "Om Praam Preem Praum Sah Shanaischaraya Namah",
        hindiSpeech = "ॐ प्रां प्रीं प्रौं सः शनैश्चराय नमः",
    ),
    Mantra(
        number = 7,
        title = "Hanuman Chalisa",
        displayText = HANUMAN_CHALISA_ENGLISH,
        hindiSpeech = HANUMAN_CHALISA_HINDI,
    ),
)
