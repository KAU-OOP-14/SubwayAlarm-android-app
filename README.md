# SubwayAlarm-android-app
실시간 지하철 도착정보 알람 앱

## 사용 기술

### - mvvm architecture
UI 로직만을 담고 있는 view, 데이터 베이스나 api 등의 data를 담고 있는 model, view의 신호를 받아서 데이터를 조작하는 비즈니스 로직만 담는 view model로 나눈 아키텍쳐.
view 는 view model을 바라보고, view model은 model을 바라보며, 데이터의 흐름이 단방향으로 이루어진다.
이 프로젝트에서는 repository, database를 model로, activity와 fragment는 view, 이들의 비즈니스 로직을 처리하는 view model로 나누어 주었다.

### - Retrofit
api를 받아올 수 있는 편리한 라이브러리로, data class에 대한 interface를 만들어 전달하면, 그에 맞는 형식으로 api를 가공해서 전해준다.
자체적으로 thread 관리가 되기 때문에, ANR문제에 대한 신경을 쓰지 않아도 돼서 편리하다.
이 프로젝트에서는 지하철 api를 가져오는 데에 사용하였다.

### - Coroutine 
동기 / 비동기 처리를 위한 라이브러리로, ANR 문제를 해결하기 위해 사용하고, firebase에서 데이터를 읽어오거나, excel 파일을 읽고 파싱하는 것 처럼 오래 걸리는 작업에서 사용한다.
이 프로젝트에서는 firebase에서 정보를 가져올 때와, 엑셀 파일을 읽는 동안의 로딩에서 사용하였다.

### - Rx java 
Live data와 비슷한 Observe Pattern으로, 함수형 프로그래밍 && 반응형 프로그래밍을 지원하는 라이브러리이다.
view model에서 disposable를 subscribe하여, api를 통한 데이터가 변경 되었을 때, 그에 해당하는 로직을 처리할 수 있다.
사용이 끝난 disposable은 clear를 통해 메모리에서 해제해야 한다.

### - Live data
또 하나의 Observe Pattern으로, view 에서 view model의 데이터가 바뀌는 것을 관찰하여, view 스스로 변경되도록 할 수 있다.
live data의 생명주기는 view model을 따라가기 때문에 주의해야 한다.
이 프로젝트에서는 api로 받아온 데이터 등을 live data로 view model에 저장하였다.

### - Koin
의존성 주입을 도와주는 라이브러리. 서로 의존성을 가지는 두 객체가 있을 때, 안드로이드가 제어의 역전을 해 주어 의존성을 외부에서 주입한다.
외부에서 의존성을 주입할 경우, unit test를 할 때 편리하고, class간의 의존도가 떨어지기 때문에 유지보수 측면에서 좋다.
이 프로젝트에서는 repository의 의존성을 갖는 view model에서, 의존성 주입을 해주어 repository를 koin이 외부에서 주입하도록 하였다.


## 작업 기간
22.10.26 ~ 22.12.6

