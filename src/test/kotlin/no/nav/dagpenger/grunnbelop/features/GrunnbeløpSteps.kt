package no.nav.dagpenger.grunnbelop.features

import io.cucumber.java8.No
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.nav.dagpenger.grunnbelop.Grunnbeløp
import no.nav.dagpenger.grunnbelop.Regel
import no.nav.dagpenger.grunnbelop.Regel.Grunnlag
import no.nav.dagpenger.grunnbelop.Regel.Minsteinntekt
import no.nav.dagpenger.grunnbelop.forDato
import no.nav.dagpenger.grunnbelop.getGrunnbeløpForRegel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale

class GrunnbeløpSteps() : No {
    private var grunnbeløp: Grunnbeløp? = null
    private var år: Int? = null
    private var verdi: BigDecimal? = null

    private companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy").localizedBy(Locale("no", "NO"))
    }

    init {
        Gitt("at grunnbeløpet for {int} er {int} kroner") { år: Int, kroner: Int ->
            this.år = år
            verdi = kroner.toBigDecimal()
            grunnbeløp = Grunnbeløp.valueOf("FastsattI$år")
            grunnbeløp!!.verdi shouldBe kroner.toBigDecimal()
        }
        Når("grunnbeløpet trer i kraft {string}") { dato: String ->
            val iverksettesFom = LocalDate.parse("$dato $år", dateFormatter)
            grunnbeløp!!.iverksattFom shouldBe iverksettesFom
        }

        Så("skal grunnlag bruke {int} kroner etter 1. mai") { kroner: Int ->
            val førsteMai = grunnbeløp ved Grunnlag den LocalDate.of(år!!, Month.MAY, 1)
            førsteMai shouldBe grunnbeløp
            førsteMai.verdi shouldBe kroner.toBigDecimal()
        }
        Og("minsteinntekt bruke {int} kroner etter {string}") { kroner: Int, hg: String ->
            val hengendeG = LocalDate.parse("$hg $år", dateFormatter)
            // Sjekk at grunnbeløpet ikke trår i kraft før dagen til hengende G har passert
            withClue("Grunnbeløpet trår i kraft for minsteinntekt før hengende G") {
                val gFørGjustering = grunnbeløp ved Minsteinntekt den hengendeG.minusDays(1)
                gFørGjustering shouldNotBe grunnbeløp
            }

            // Sjekk at grunnbeløpet etter gjustering (hengende G) er riktig
            val gJustering = grunnbeløp ved Minsteinntekt den hengendeG
            gJustering shouldBe grunnbeløp
            gJustering.verdi shouldBe kroner.toBigDecimal()
        }
    }

    fun Regel.grunnbeløp(
        dato: LocalDate,
        iverksattFom: LocalDate,
    ) = getGrunnbeløpForRegel(this).forDato(dato, iverksattFom)
}

private infix fun Grunnbeløp?.ved(regel: Regel) =
    object {
        infix fun den(dato: LocalDate) = getGrunnbeløpForRegel(regel).forDato(dato, this@ved!!.iverksattFom)
    }
