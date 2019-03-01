import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)

export const i18n = new VueI18n({
  locale: 'pl',
  fallbackLocale: 'pl',
  messages: {
    pl: {
      home: {
        service1: 'Wyszukaj konkordancję',
        service2: 'niedostępny',
        service3: '...'
      },
      navbar: {
        home: 'Strona główna',
        about: 'O korpusie',
        services: 'Usługi',
        concordance: 'Konkordancje'
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
      concordance: {
        word: 'Leksem:',
        word_i: 'wpisz wyraz',
        corpus: 'Korpus:',
        corpus_i: 'wybierz korpus',
        method: 'Forma wyrazu:',
        method_i: 'wybierz jedną z form wprowadzonego wyrazu ',
        howmany: 'Liczba wyników na stronę:',
        base: 'Forma bazowa wyrazu',
        orth: 'Forma zmieniona wyrazu',
        submit: 'Wyszukaj konkordancję',
        loading: 'Wczytywanie...',
        warning: 'Wprowadź wyraz aby rozpocząć wyszukiwanie'
      },
      footer: {
        copyright: 'Copyright 2019 Clarin-PL. Wszystkie prawa zastrzeżone.'
      }
    },
    en: {
      home: {
        service1: 'Find corcondance',
        service2: 'unavailable',
        service3: '...'
      },
      navbar: {
        home: 'Home',
        about: 'About Corpus',
        services: 'Services',
        concordance: 'Concordance'
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
      concordance: {
        word: 'Lexeme:',
        word_i: 'type word',
        corpus: 'Corpus:',
        corpus_i: 'select corpus',
        method: 'Form:',
        method_i: 'select form of word',
        howmany: 'Ammount of results on page:',
        base: 'Base form',
        orth: 'Changed form',
        submit: 'Find concordance',
        loading: 'Loading...',
        warning: 'Enter a word to start the search'
      },
      footer: {
        copyright: 'Copyright 2019 Clarin-PL. All Rights Reserved.'
      }
    }
  }
})
