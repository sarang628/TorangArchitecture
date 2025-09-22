https://developer.android.com/kotlin/flow

flow는 [coroutine]()에서 
하나의 값만 리턴하는 [suspend function]()과는 반대로 
순차적으로 값들을 방출 할 수 있음.

Flow는 coroutine의 상단에서 빌드되며, 여러 값들을 제공한다.
Flow은 컨셉적으로 비동기적으로 계산할 수 있는 데이터 스트림이다.
방출된 값은 반드시 같은 타입의 값이다.
예를들어 Flow<Int>는 Integer 값들만을 방출한다.

Flow는 Iterator와 흡시하다. 
하지만 suspend function을 사용하여 비동기적으로 값들을 생산하고 소비한다.
이 의미는 flow는 메인 thread의 blocking 없이 값을 생성하는 안전한 네트워크 요청을 할 수 있다.

다음 3가지 엔티티가 스트림 데이터에 있다.
'생산자'는 스트림에 추가된 데이터를 생성한다. 코루틴 덕분에 Flow는 비동기로 데이터를 생성할 수도 있다.
(옵션) '중개자'는 스트림 안에서 방출되는 값 또는 스트림 자체를 수정할 수 있다.
'소비자'는 스트림의 값들을 소비한다.

<img src = "./flow-entities.png" width="500"/>

안드로이드에서 저장소는 (데이터를 디스플레이하는 UI의 소비자인) UI 데이터의 전형적으로 생산자이다.
한편 UI 레이어는 사용자 입력 이벤트의 생산자 이고 계층의 다른 레이어들은 이를 소비.
생산자와 소비자 사이의 레이어들은 (스트림 데이터를 다음 레이어의 요청에 맞게 수정하는) 중재자로서 동작한다.


https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/

순차적으로 데이터를 방출하고 정상적 종료 또는 exception을 발생시키는 비동기 데이터 스트림

flow에서 중간 연사자로는 map, filter, take, zip, etc 함수로 상위 스트림에 적용하여 하위 스트림으로 내보낸다.

중간 연산자 안에서는 어떤 코드도 실행 시켜서는 안됨. suspend 함수를 사용하는것도 안됨.

다음 실행과 빠른 반환을 위해 오직 연산자들을 체인으로 설정.

콜드 flow 속성으로 알려진다.

종료 연산자는 suspending 함수들인 collect, single, reduce, toList 등 이거나 주어진 스코프에서 flow 의 수집을 시작하는 launchIn 연산자 이다.

이것들은 업스트림 flow에서 적용되고 모든 연산자의 실행을 트리거 한다.

flow의 실행은 또한 flow 수집이 호출되고, blocking 없는 suspending에서 수행 된다.

종료 연산자들은 정상 종료 또는 성공 또는 실패에 따른 예외를 모든 flow 연산자의 상위 연산자에서 실행

가장 기본적은 종료 연산자는 collect 이다. 예

```
try {
    flow.collect { value ->
        println("Received $value")
    }
} catch (e: Exception) {
    println("The flow has thrown an exception: $e")
}
```

기본적으로 flow는 순차적이고 모든 flow 연산자들이 같은 코루틴에서 순차적으로 실행된다.
buffer 이나 flatMapMerge와 같은 flow 실행에 동시성 도입을 위해 특별히 제작된 몇 연산자를 위한 예외도 있음.

flow 인터페이스는 (반복적으로 수집할 수 있고 수집되었을 때마다 코드를 실행하는) 콜드스트림 인지 
( 각 수집에서 같이 동작하는 소스로부터 다른 값을 방출하는 )핫스트림인지 정보를 포함하지 않는다.

플로우는 주로 콜드 스트림. 하지만 핫 스트림을 대표하는 SharedFlow 타입이 있다.

추가로, 어떤 flow 라도 stateIn 과 sharedIn 또는 flow를 hot 채널로 변환하는 produceIn 연산자에서 핫으로 바뀔 수 있다.
