[![](https://jitpack.io/v/navikt/dp-grunnbelop.svg)](https://jitpack.io/#navikt/dp-grunnbelop)

# dp-grunnbelop

Bibliotek som holder nåværende og
historiske [grunnbeløp](https://www.nav.no/no/nav-og-samfunn/kontakt-nav/utbetalinger/grunnbelopet-i-folketrygden) for
Dagpenger domenet

## Grunnbeløp

[Grunnbeløp vi støtter og tilhørende gyldighetsperioder](docs/Grunnbeløp.approved.md).

## G-justering

Grunnbeløpet (som vi ofte kaller G) justeres 1. mai hvert år og blir fastsatt etter trygdeoppgjøret.

Grunnbeløpet blir fastsett av Stortinget hvert år i takt med forventa lønns- og prisutvikling. Dette skjer vanligvis i
slutten av mai eller starten av juni måned og får tilbakevirkning fra mai.

For Dagpengers del er G-justering en reberegning av grunnlag (§4-11) og sats (§4-12) for vedtak som er innvilget fra 1.
mai til datoen den ny G'en er iverksatt. PT gjøres uttrekk av hvilke vedtak som skal justeres fra Arena.
I tillegg må den ny G'en legges til av beregning av krav til minste arbeidsinntekt (§4-4) og dagpengeperiode (§ 4-15)

Fremgangsmåte:

1. Legg til ny G i [Grunnbelop](src/main/kotlin/no/nav/dagpenger/grunnbelop/Grunnbelop.kt)
   (Bruker eksempel fra 2022)

```kotlin
FastsattI2022(verdi = 111477.toBigDecimal(), iverksattFom = LocalDate.of(2022, Month.MAY, 21))
```

- `verdi` er ny G-verdi
- `iverksattFom` er datoen ny G skal iverksettes fra. Dette er datoen selve g-justeringen skal kjøres. I 2022 var dette
  lørdag 21. mai

2. Legg til `gyldighetsperioder` [for grunnbeløp](src/main/kotlin/no/nav/dagpenger/grunnbelop/Grunnbelop.kt) for
   henholdsvis dagpengegrunnlag og minsteinntekt

(Bruker eksempel fra 2022)

```kotlin
Grunnbeløp.FastsattI2022 to mapOf(
    Regel.Grunnlag to Gyldighetsperiode(
        fom = LocalDate.of(2022, Month.MAY, 1)
    ),
    Regel.Minsteinntekt to Gyldighetsperiode(
        fom = LocalDate.of(2022, Month.MAY, 23)
    )
)
```

- `Regel.Grunnlag` definerer når ny G skal gjelde fra for grunnlag (§4-11) og sats (§4-12). Den vil være fra
  justeringsdato 1. mai
- `Regel.Minsteinntekt` definerer når ny G skal gjelde for krav til minste arbeidsinntekt (§4-4) og dagpengeperiode (§
  4-15). Også kalt hengende-G. Fra-og-med (fom) gjelder dermed fra første mandag etter at grunnbeløpet er fastsatt i
  stortinget, slik at nye dagpengesaker behandles med samme, gammel sats når de innvilges tilbake i tid (fra 1. mai og
  frem til dato for hengende G)

Datoene over må ikke forveksles med `iverksattFom` som gjenspeiler når ny G skal _iverksettes_.

3. Lag tester for å teste grensedatoene
   i [GrunnbelopTest](src/test/kotlin/no/nav/dagpenger/grunnbelop/GrunnbelopTest.kt)

4. Lag en commit og push endringene til Github. Vent på bygg og at ny versjon av Grunnbeløp
   lanseres (https://github.com/navikt/dp-grunnbelop/releases)
    1. [Eksempel pull-request fra 2022](https://github.com/navikt/dp-grunnbelop/pull/1/files)

5. Oppdaterer repoene (i skrivende stund):
    1. dp-regel-minsteinntekt (§ 4-4)
    2. dp-regel-periode (§ 4-15)
    3. dp-regel-grunnlag (§ 4-11)
    4. dp-regel-sats (§ 4-12)
    5. dp-oppslag-inntekt (Brukes av dp-quiz for å hente Grunnbeløp)

Praksis er at vi oppretter pull-request i disse repoene og merger før G-justeringsdato. De kan deployes uavhengig av
Arene. `iverksattFom` vil styre iverksettingen.

Se rutine i Arena https://confluence.adeo.no/display/TEAMARENA/Rutine+for+G-regulering+i+Arena 
