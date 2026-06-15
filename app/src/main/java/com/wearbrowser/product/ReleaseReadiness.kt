package com.wearbrowser.product

/** Non-code release checklist kept near the product code so it is visible to contributors. */
object ReleaseReadiness {
    val requiredBeforeStoreRelease = listOf(
        "Run assembleDebug and assembleRelease in CI.",
        "Verify gestures on one round Wear OS device/emulator and one square Android small-screen target.",
        "Record screenshots for round and square layouts.",
        "Confirm clear-data behavior removes history, tabs, cache and site profiles.",
        "Confirm URL normalization never treats unsafe schemes as web pages.",
        "Prepare privacy policy and release notes."
    )
}
