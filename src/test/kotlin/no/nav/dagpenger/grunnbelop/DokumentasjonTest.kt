package no.nav.dagpenger.grunnbelop

import com.spun.util.persistence.Loader
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import no.nav.dagpenger.grunnbelop.Grunnbeløp.GjusteringsTest
import no.nav.dagpenger.grunnbelop.Regel.Grunnlag
import no.nav.dagpenger.grunnbelop.Regel.Minsteinntekt
import org.approvaltests.Approvals
import org.approvaltests.core.Options
import org.approvaltests.namer.NamerWrapper
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.nio.file.Paths
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.io.path.absolutePathString

class DokumentasjonTest {
    @Test
    fun `Dokumenter alle grunnbeløp`() {
        val testedeGrunnbeløp = mutableSetOf<DefinertGrunnbeløp>()

        // Test alle grunnbeløp
        Grunnbeløp.values().filterNot { it == GjusteringsTest }.forAll { grunnbeløpUnderTest ->
            val dagensDato = LocalDate.now() // Genereres utifra datoen testen kjøres
            val grunnlagFom = gyldighetsperioder[grunnbeløpUnderTest]!![Grunnlag]!!.fom
            val minsteinntektFom = gyldighetsperioder[grunnbeløpUnderTest]!![Minsteinntekt]!!.fom
            val d =
                DefinertGrunnbeløp(
                    grunnbeløpUnderTest.iverksattFom,
                    grunnbeløpUnderTest.verdi,
                    grunnlagFom,
                    minsteinntektFom,
                ).also { testedeGrunnbeløp.add(it) }

            if (!d.erIverksatt) {
                return@forAll
            }

            // Test grunnlag
            getGrunnbeløpForRegel(Grunnlag)
                .forDato(
                    dato = grunnlagFom,
                    gjeldendeDato = dagensDato,
                ).verdi shouldBe grunnbeløpUnderTest.verdi

            // Test minsteinntekt
            getGrunnbeløpForRegel(Minsteinntekt)
                .forDato(
                    dato = minsteinntektFom,
                    gjeldendeDato = dagensDato,
                ).verdi shouldBe grunnbeløpUnderTest.verdi
        }

        lagDokumentasjon(testedeGrunnbeløp.toList())
    }

    private fun lagDokumentasjon(grunnbeløp: List<DefinertGrunnbeløp>) {
        fun varselOmFramtidig(grunnbeløp: DefinertGrunnbeløp) = if (grunnbeløp.erIverksatt) "" else " 🔮"

        val rader =
            grunnbeløp.joinToString("\n") { "| ${it.fom}${varselOmFramtidig(it)} | ${it.grunnbeløp} | ${it.g} | ${it.hg} |" }

        @Language("Markdown")
        val markdown =
            """
            ># Grunnbeløp
            >
            >* G = Grunnlag + sats
            >* HG = Minsteinntekt + periode
            >
            >| Fra og med | Grunnbeløp | G | HG | 
            >|------------|------------|---|----|
            >$rader
            > 
            >
            """.trimMargin(">")
        skriv("Grunnbeløp", markdown)
    }

    private data class DefinertGrunnbeløp(
        private val iverksattFom: LocalDate,
        private val verdi: BigDecimal,
        private val grunnlagFom: LocalDate,
        private val minsteinntektFom: LocalDate,
    ) {
        val erIverksatt = dagensdato.isAfter(iverksattFom)

        val fom: String = iverksattFom.format(norwegianDateFormat)
        val grunnbeløp: String = norwegianNumberFormat.format(verdi)
        val g: String = grunnlagFom.format(norwegianDateFormat)
        val hg: String = minsteinntektFom.format(norwegianDateFormat)

        private companion object {
            private val locale = Locale("no", "NO")
            private val norwegianDateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale)
            private val norwegianNumberFormat = NumberFormat.getCurrencyInstance(locale)

            private val dagensdato: LocalDate = LocalDate.now()
        }
    }

    private fun skriv(
        tittel: String,
        dokumentasjon: String,
    ) {
        Approvals.namerCreater = Loader { NamerWrapper({ tittel }, { path }) }
        Approvals
            .verify(
                dokumentasjon,
                options,
            )
    }

    private companion object {
        val path = Paths.get("./docs").absolutePathString()
        val options: Options = Options().forFile().withExtension(".md")
    }
}
