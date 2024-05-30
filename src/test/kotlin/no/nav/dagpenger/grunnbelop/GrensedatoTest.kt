package no.nav.dagpenger.grunnbelop

import io.kotest.matchers.shouldBe
import no.nav.dagpenger.grunnbelop.Regel.Grunnlag
import no.nav.dagpenger.grunnbelop.Regel.Minsteinntekt
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

class GrensedatoTest {
    @Test
    fun ` Skal returnere grunnbeløp for 2024 etter iverkssatt FOM dato `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2024, Month.MAY),
            gjeldendeDato = LocalDate.of(2024, 5, 3),
        ).verdi shouldBe Grunnbeløp.FastsattI2023.verdi

        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2024, Month.MAY),
            gjeldendeDato = LocalDate.of(2024, 6, 1),
        ).verdi shouldBe Grunnbeløp.FastsattI2024.verdi

        getGrunnbeløpForRegel(Minsteinntekt).forMåned(
            dato = YearMonth.of(2024, Month.JUNE),
            gjeldendeDato = LocalDate.of(2024, 6, 1),
        ).verdi shouldBe Grunnbeløp.FastsattI2024.verdi
    }

    @Test
    fun ` Skal returnere grunnbeløp for 2023 etter iverkssatt FOM dato `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2023, Month.MAY),
            gjeldendeDato = LocalDate.of(2023, 5, 27),
        ).verdi shouldBe 118620.toBigDecimal()

        getGrunnbeløpForRegel(Minsteinntekt).forMåned(
            dato = YearMonth.of(2023, Month.JUNE),
            gjeldendeDato = LocalDate.of(2023, 5, 29),
        ).verdi shouldBe 118620.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp for 2022 dersom gjeldendedato er før iverksatt dato for 2023 G`() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2023, Month.MAY),
            gjeldendeDato = LocalDate.of(2022, 5, 27),
        ).verdi shouldBe 111477.toBigDecimal()

        getGrunnbeløpForRegel(Minsteinntekt).forMåned(
            dato = YearMonth.of(2023, Month.MAY),
            gjeldendeDato = LocalDate.of(2023, 5, 28),
        ).verdi shouldBe 111477.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp for 2022 etter iverkssatt FOM dato `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2022, Month.MAY),
            gjeldendeDato = LocalDate.of(2022, 5, 21),
        ).verdi shouldBe 111477.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp for 2021 dersom gjeldendedato er før iverksatt dato for 2022 G`() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2022, Month.MAY),
            gjeldendeDato = LocalDate.of(2022, 5, 20),
        ).verdi shouldBe 106399.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp 2021 etter iverkssatt FOM dato `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2021, Month.MAY),
            gjeldendeDato = LocalDate.of(2021, 5, 24),
        ).verdi shouldBe 106399.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp for 2020 dersom gjeldendedato er før iverksatt dato for 2021 G`() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2021, Month.MAY),
            gjeldendeDato = LocalDate.of(2021, 5, 22),
        ).verdi shouldBe 101351.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 101351 for måned mai 2020 `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            dato = YearMonth.of(2020, Month.MAY),
            gjeldendeDato = LocalDate.of(2020, 9, 21),
        ).verdi shouldBe 101351.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 99858 for måned april 2020 `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            YearMonth.of(2020, Month.APRIL),
        ).verdi shouldBe 99858.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 99858 for måned may 2019 `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            YearMonth.of(2019, Month.MAY),
        ).verdi shouldBe 99858.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 96883 for måned april 2019 `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            YearMonth.of(2019, Month.APRIL),
        ).verdi shouldBe 96883.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 93634 for måned mars 2018 `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            YearMonth.of(2018, Month.MARCH),
        ).verdi shouldBe 93634.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 92576 for måned mai 2016  `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            YearMonth.of(2016, Month.MAY),
        ).verdi shouldBe 92576.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 90068 for måned august 2015 `() {
        getGrunnbeløpForRegel(Grunnlag).forMåned(
            YearMonth.of(2015, Month.AUGUST),
        ).verdi shouldBe 90068.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 90068 for dato 06082015 `() {
        getGrunnbeløpForRegel(Grunnlag).forDato(
            LocalDate.of(2015, 8, 6),
        ).verdi shouldBe 90068.toBigDecimal()
    }

    @Test
    fun ` Skal returnere grunnbeløp på 90068 for dato 01052019 `() {
        getGrunnbeløpForRegel(Grunnlag).forDato(
            LocalDate.of(2019, 5, 1),
        ).verdi shouldBe 99858.toBigDecimal()
    }
}
