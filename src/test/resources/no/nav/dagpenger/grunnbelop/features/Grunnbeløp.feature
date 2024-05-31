#language: no
Egenskap: Grunnbeløp

  Scenariomal: Grunnbeløp endres en gang i året
    Gitt at grunnbeløpet for <år> er <beløp> kroner
    Når grunnbeløpet trer i kraft "<iverksettelsesdato>"
    Så skal grunnlag bruke <beløp> kroner etter 1. mai
    Og minsteinntekt bruke <beløp> kroner etter "<hengende g>"

    Eksempler:
      | år   | beløp  | iverksettelsesdato | hengende g    |
      | 2024 | 124028 | 1. juni            | 3. juni       |
      | 2023 | 118620 | 27. mai            | 29. mai       |
      | 2022 | 111477 | 21. mai            | 23. mai       |
      | 2021 | 106399 | 23. mai            | 24. mai       |
      | 2020 | 101351 | 19. september      | 21. september |
      | 2019 | 99858  | 26. mai            | 27. mai       |
      | 2018 | 96883  | 3. juni            | 4. juni       |
      | 2017 | 93634  | 28. mai            | 29. mai       |
      | 2016 | 92576  | 29. mai            | 30. mai       |
      | 2015 | 90068  | 31. mai            | 1. juni       |