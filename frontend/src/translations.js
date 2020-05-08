import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)

export const i18n = new VueI18n({
  locale: 'pl',
  fallbackLocale: 'pl',
  messages: {
    pl: {
      nodata: 'Nie uzyskano żadnych danych dla wybranej konfiguracji',
      scale: {
        title: 'kliknj aby zmienić skalę',
        true: '✔ skala logarytmiczna',
        false: '✘ skala logarytmiczna'
      },
      cancel: 'Anuluj',
      undo_selection: '↺ cofnij wybór',
      home: {
        title: 'Portal tekstów prasowych',
        service1: 'Konkordancje',
        service2: 'Listy frekwencyjne',
        service3: 'Profile wyrazów',
        service4: 'Analiza ilościowa',
        service5: 'Mapa Nazw Miejscowych',
        service6: 'Szeregi czasowe'
      },
      navbar: {
        home: 'Strona główna',
        about: 'O korpusie',
        services: 'Usługi',
        concordance: 'Konkordancje',
        documents: 'Przegląd próbek',
        form_tooltip: 'Wpisz wyraz lub frazę i wyszukaj wystąpienia.'
      },
      about: {
        project: {
          header: 'O projekcie',
          content: 'Korpus i wyszukiwarka ChronoPress powstały w ramach prac konsorcjum naukowego „CLARIN – Polskie wspólne zasoby językowe i infrastruktura technologiczna”. CLARIN-PL jest częścią  „Polskiej Mapy Drogowej Infrastruktury Badawczej” oraz „European Roadmap for Research Infrastructures, European Strategy Forum on Research Infrastructures”. Zadanie finansowane było z funduszy MNiSW.'
        },
        tools: {
          header: 'Narzędzia',
          content: 'ChronoPress wykorzystuje lematyzator języka polskiego (Tager WCRFT2 z analizatorem morfologicznym Morfeusz 2 ), dzięki czemu możliwe jest generowanie „przekrojów” morfosyntaktycznych korpusu, czyli podzbiorów złożonych z konkretnych części mowy. Ponadto użytkownik może korzystać z narzędzia automatycznego rozpoznawania jednostek nazewniczych (Liner), a także niektórych klas semantycznych (w oparciu o Słowosieć i dedykowane zasoby leksykalne). Z punktu widzenia użytkownika specyficzną cechą korpusu ChronoPress jest implementacja narzędzi do analizy ilościowej tekstu (generowanie list frekwencyjnych, obliczanie parametrów ilościowych) oraz szeregów czasowych, czyli wykresów wartości parametrów leksykalnych w czasie.'
        },
        corpus: {
          header: 'Korpus',
          content: 'Korpus ChronoPress zawiera ok. 56000 starannie dobranych fragmentów tekstów prasowych (użytych na zasadach cytatu), opracowanych językowo na poziomie morfosyntaktycznym i ustrukturyzowanych pod względem chronologii. Próbki mają długość ok. 300 wyrazów tekstowych i reprezentują pełne spektrum tematyczne oficjalnego dyskursu publicznego lat 1945–1954 (średnio 12 różnych tytułów gazet lub czasopism na rok). Znakomitą większość tekstów pozyskano z zasobów bibliotek wrocławskich: Biblioteki Uniwersytetu Wrocławskiego i Zakładu Narodowego im. Ossolińskich. Wykorzystywano także cyfrowe wersje niektórych czasopism, udostępnione przez Federację Bibliotek Cyfrowych. Przy pozyskaniu materiałów z 1945 roku pomoc zdobyciu tekstów okazały Biblioteka Uniwersytetu Warszawskiego, Centralna Biblioteka Wojskowa im. Józefa Piłsudskiego oraz Biblioteka Uniwersytecka im. Jerzego Giedroycia w Białymstoku.'
        },
        team: {
          header: 'Zespół',
          content: 'Twórcą koncepcji i koordynatorem prac nad korpusem ChronoPress jest prof. dr hab. Adam Pawłowski z Uniwersytetu Wrocławskiego (Instytut Informacji Naukowej i Bibliotekoznawstwa). Proces akwizycji materiału tekstowego realizował zespół ok. 60 osób – studentów, doktorantów i pracowników Uniwersytetu Wrocławskiego. Prace programistyczne w fazie testowej projektu prowadzili współpracownicy konsorcjum CLARIN z Uniwersytetu Łódzkiego, o ostateczną wersję portalu i wyszukiwarki przygotowali pracownicy Politechniki Wrocławskiej.'
        }
      },
      task: {
        waiting_for_response: 'Czekam na odpowiedź serwera...',
        loading_data: 'Wczytuję dane do widoku...'
      },
      concordance: {
        word: 'Leksem',
        word_i: 'szybkie wyszukiwanie konkordancji',
        corpus: 'Korpus',
        corpus_i: 'wybierz korpus',
        method: 'Forma wyrazu',
        method_i: 'wskaż formę wprowadzonego wyrazu',
        howmany: 'Liczba wyników na stronie',
        base: 'Forma hasłowa (leksem)',
        orth: 'Forma zmieniona wyrazu',
        submit: 'Wyszukaj konkordancję',
        loading: 'Wczytuję dane...',
        warning: 'Wprowadź wyraz przed rozpoczęciem wyszukiwania',
        document_info: 'Kliknij aby zobaczyć szczegóły dokumentu',
        found1: 'wynik',
        found2: 'wyniki',
        found3: 'wyników',
        showfilters: 'Pokaż Filtry ⇓ ',
        hidefilters: 'Ukryj filtry ⇑ ',
        modal_title: 'Dane próbki',
        modal_cancel: 'Wyjdź',
        proper_names: 'Nazwy własne'
      },
      footer: {
        copyright: 'Copyright 2020 Clarin-PL. Wszystkie prawa zastrzeżone.'
      },
      frequency: {
        title: 'Lista frekwencyjna',
        submit: 'Utwórz listę frekwencyjną',
        true: 'Formy hasłowe (leksemy)',
        false: 'Formy odmienione',
        stop_list: 'Stop-lista'
      },
      wordprofiles: {
        word: 'Wpisz wyraz',
        word_part: 'Wybierz część mowy wyrazu',
        left: 'Lewy kontekst',
        right: 'Prawy kontekst',
        context: 'Część mowy dla kontekstu',
        submit: 'Generuj profil'
      },
      quantity_analysis: {
        submit: 'Generuj statystyki',
        word: 'wyraz',
        sentence: 'zdanie',
        average: 'histogram długości',
        zipf_histogram: 'histogram Zipfa',
        letter: 'litera',
        syllable: 'sylaba',
        phoneme: 'fonem',
        calculation_object: 'Przedmiot obliczeń',
        calculation_type: 'Rodzaj obliczeń',
        calculation_unit: 'Jednostka obliczeniowa',
        parts_of_speech: 'Część mowy',
        y_label: 'Ilość',
        x_prefix: 'Liczba jednostek (',
        x_sufix: ') w przedmiocie obliczeń (',
        alert: 'należy wybrać conajmniej jedną część mowy',
        more_info: 'więcej informacji',
        skewness: 'Skośność',
        average_length: 'Średnia długość',
        standard_deviation: 'Odchylenie standardowe',
        coefficient_of_variation: 'Współczynnik zmienności',
        kurtosis: 'Kurtoza'
      },
      exp: {
        first: 'Pierwsza strona',
        middle: 'Środkowa strona',
        last: 'Ostatnia strona'
      },
      exposition: 'Ekspozycja',
      journal_title: 'Tytuł periodyku',
      medium: 'Środek przekazu',
      period: 'Okresowość',
      publication_day: 'Dzień publikacji',
      publication_month: 'Miesiąc publikacji',
      publication_year: 'Rok publikacji',
      publication_date: 'Data publikacji',
      status: 'Status',
      style: 'Styl',
      article_title: 'Tytuł artykułu',
      authors: 'Autorzy',
      adjective: 'Przymiotnik',
      noun: 'Rzeczownik',
      verb: 'Czasownik',
      adverb: 'Przysłówek',
      day: 'Dzień',
      month: 'Miesiąc',
      year: 'Rok',
      download: 'Pobierz',
      download_csv: 'eksportuj do .tsv',
      download_xlsx: 'eksportuj do .xlsx',
      wait_for_file: ' plik jest przygotowywany',
      names_map: {
        popup: {
          full_name: 'Nazwa miejscowa',
          type: 'Typ',
          frequency: 'Częstość',
          concordance: 'Wyszukaj konkordancje'
        },
        submit: 'Pokaż Mapę'
      },
      time_series: {
        word: 'Wyraz',
        word_i: 'wprowadź wyraz',
        part_of_speech: 'Część mowy',
        time_unit: 'Jednostka czasu',
        time_unityear: 'Jednostka czasu (rok)',
        time_unitmonth: 'Jednostka czasu (miesiąc)',
        frequency: 'Liczba wystąpień'
      }
    },
    en: {
      nodata: 'No data was obtained for the selected configuration',
      scale: {
        title: 'click to change the scale',
        true: 'logarithmic scale',
        false: 'logarithmic scale'
      },
      cancel: 'Cancel',
      undo_selection: '↺ undo the selection',
      home: {
        title: 'Portal of press texts',
        service1: 'Concordances',
        service2: 'Frequency Lists',
        service3: 'Word Profiles',
        service4: 'Quanity Analysis',
        service5: 'Proper Names Map',
        service6: 'Time Series'
      },
      navbar: {
        home: 'Home',
        about: 'About Corpus',
        services: 'Services',
        concordance: 'Concordance',
        documents: 'Samples',
        form_tooltip: 'Type a word or phrase and search occurence.'
      },
      about: {
        project: {
          header: 'About project',
          content: 'ChronoPress Corpus and Search engine were created as part of the work of the scientific consortium "CLARIN - Polish Common Language Resources and Technology Infrastructure". CLARIN-PL is a part of the "Polish Road Map of Research Infrastructure" and the "European Roadmap for Research Infrastructures, European Strategy Forum on Research Infrastructures". The task was financed from the Ministry of Science and Higher Education funds.'
        },
        tools: {
          header: 'Tools',
          content: 'ChronoPress uses the Polish language lematizer (Tager WCRFT2 with morphological analyzer Morfeusz 2 ) to generate morphosyntactic corpus "cross-sections", i. e. sub-sets consisting of specific parts of speech. In addition, the user can also use the tool of automatic naming unit recognition (Liner), as well as some semantic classes (based on Słowosieć (Polish wordnet) and dedicated lexical resources). From the users point of view, a specific feature of the ChronoPress corpus is the implementation of tools for quantitative analysis of text (generating turnout lists, calculating quantitative parameters) and time series, i. e. graphs of lexical parameters over time.'
        },
        corpus: {
          header: 'Corpus',
          content: 'The ChronoPress corpus contains approx. 56000 carefully selected excerpts of press texts (used on the basis of quotation rules), prepared linguistically at the morphosyntactic and chronologically structured level. The samples are approximately approx. 300 text words and represent the full thematic spectrum of the official public discourse in 1945-1954 (average of 12 different titles of newspapers or periodicals per year). The vast majority of the texts were obtained from the resources of Wroclaw libraries: the Library of the University of Wroclaw and the Wroclaw National Institute. Ossolinski. Digital versions of some magazines were also used, made available by the Digital Libraries Federation. In order to obtain material from 1945, the Library of the University of Warsaw, the Central Military Library of the Fryderyk Chopin University of Warsaw, and the Library of the Polish Army, Warsaw University, helped to find texts. The Museum of the History of Polish Jews was founded by Professor Józef Pi?sudski and the University Library of St. Joseph Pi?sudski. Jerzy Giedroyc in Białystok.'
        },
        team: {
          header: 'Team',
          content: 'The concept creator and coordinator of the work on the ChronoPress corps is Prof. Dr. hab. Adam Pawłowski from the University of Wroclaw (Institute for Scientific Information and Library Science). The process of acquisition of text material was carried out by a group of approx. 60 people - students, doctoral students and employees of the University of Wroclaw. Programming works in the testing phase of the project were carried out by the CLARIN consortium collaborators from the University of?ód?, the final version of the portal and search engine were prepared by the employees of the Wroclaw University of Technology.'
        }
      },
      task: {
        waiting_for_response: 'Waiting for server response...',
        loading_data: 'Loading data into the view...'
      },
      concordance: {
        word: 'Lexeme',
        word_i: 'quick concordance search',
        corpus: 'Corpus',
        corpus_i: 'select corpus',
        method: 'Form',
        method_i: 'select form of word',
        howmany: 'Ammount of results on page',
        base: 'Base form',
        orth: 'Changed form',
        submit: 'Find concordance',
        loading: 'Loading...',
        warning: 'Enter a word to start the search',
        document_info: 'Click to view the document details',
        found1: 'result',
        found2: 'results',
        found3: 'results',
        showfilters: '⇓ Show filters',
        hidefilters: '⇑ Hide  filters',
        modal_title: 'Data sample',
        modal_cancel: 'Close',
        proper_names: 'Proper names'
      },
      frequency: {
        title: 'Frequency list',
        submit: 'Create frequency list',
        true: 'Frequency Lists of words',
        false: 'Frequency Lists of lexemes (form slogans)',
        stop_list: 'Stopwords'
      },
      wordprofiles: {
        word: 'Type word',
        word_part: 'part of the speech of the word',
        left: 'Left context',
        right: 'Right context',
        context: 'part of the speech of the context',
        submit: 'Generate profile'
      },
      quantity_analysis: {
        submit: 'Generate statistics',
        word: 'word',
        sentence: 'sentence',
        average: 'average',
        zipf_histogram: 'zipf_histogram',
        letter: 'letter',
        syllable: 'syllable',
        phoneme: 'phoneme',
        calculation_object: 'Calculation object',
        calculation_type: 'Calculation type',
        calculation_unit: 'Calculation unit',
        parts_of_speech: 'Parts of speech',
        y_label: 'Quantity',
        x_prefix: 'Units (',
        x_sufix: ') in calculation object (',
        alert: 'at least one part of the speech should be chosen',
        more_info: 'more details',
        skewness: 'Skewness',
        average_length: 'Average length',
        standard_deviation: 'Standard deviation',
        coefficient_of_variation: 'Coefficient of variation',
        kurtosis: 'Kurtosis'
      },
      footer: {
        copyright: 'Copyright 2020 Clarin-PL. All Rights Reserved.'
      },
      exp: {
        first: 'First page',
        middle: 'Middle page',
        last: 'Last page'
      },
      exposition: 'Exposure',
      journal_title: 'Journal Title',
      medium: 'Medium',
      period: 'Periodic',
      publication_day: 'Publication day',
      publication_month: 'Publication month',
      publication_year: 'Publication year',
      publication_date: 'Publication date',
      status: 'Status',
      style: 'Style',
      article_title: 'Article title',
      authors: 'Authors',
      adjective: 'Adjective',
      noun: 'Noun',
      verb: 'Verb',
      adverb: 'Adverb',
      day: 'Day',
      month: 'Month',
      year: 'Year',
      download: 'Download',
      download_csv: 'export to .tsv',
      download_xlsx: 'export to .xlsx',
      wait_for_file: ' the file is being prepared',
      names_map: {
        popup: {
          full_name: 'Proper name',
          type: 'Type',
          frequency: 'Frequency',
          concordance: 'Find corcondances'
        },
        submit: 'Show map'
      },
      time_series: {
        word: 'Word',
        word_i: 'insert word',
        part_of_speech: 'Part of Speech',
        time_unit: 'Time unit',
        time_unityear: 'Time unit (year)',
        time_unitmonth: 'Time unit (month)',
        frequency: 'Frequency'
      }
    }
  }
})
