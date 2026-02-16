package io.github.stiv3ns.twactionorganizer.core.villages

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class VillageTest : WordSpec({
    "Village.toString" should {
        "return proper representation" {
            val village = StubVillageFactory.getVillage()
            village.toString() shouldBe "${village.x}|${village.y}"
        }
    }

    "Village.distance" should {
        "return proper squared distance between given villages" {
            StubVillageFactory.getVillage(500, 500) distanceTo
                StubVillageFactory.getVillage(500, 501) shouldBe 1

            StubVillageFactory.getVillage(500, 500) distanceTo
                StubVillageFactory.getVillage(500, 510) shouldBe 100

            StubVillageFactory.getVillage(500, 500) distanceTo
                StubVillageFactory.getVillage(523, 614) shouldBe 13525

            StubVillageFactory.getVillage(318, 219) distanceTo
                StubVillageFactory.getVillage(411, 200) shouldBe 9010
        }
    }
})
