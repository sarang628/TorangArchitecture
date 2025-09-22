https://developer.android.com/topic/architecture/ui-layer

UI layer

UI의 역할은 앱 데이터를 화면에 표시와 사용자와 상호작용을 전달
사용자와 사용작용 또는 외부 입력 (네트워크 통신) 등으로 데이터가 변경될 때마다
UI는 이 변화에 대응하여 업데이트.
사실상, 데이터 레이어에서 받은 상태의 표시이다.

하지만 데이터 레이어에서 받은 데이터는 표시해야하는 데이터와 포맷이 다름
예를들어 일부 데이터만 필요 할 수 있음.
또는 2가지 다른 데이터를 합쳐서 표시해야할 수도 있음.

적용한 로직에 관계 없이, UI가 전체적인 랜더를 필요로 할 때, 모든 정보를 넘겨야 한다.
UI 레이어는 앱 데이터를 UI에 표시할 수 있는 데이터로 변환해주는 파이프라인 이다.

Note: 이 장에서 추천 모범 사례들은 대부분 종류의 앱에 스케일, 품질과 강력함을 항상, 쉬운 테스트를 할 수 있게 해준다.
하지만 가이드라인을 이해 준수하고, 서비스 하는 앱의 요구사항에 맞게 적용해야 한다.

# [A basic case study](https://developer.android.com/topic/architecture/ui-layer#case-study)

사용자가 읽을 뉴스 기사를 가져오는 앱을 가정.

앱은 읽을 수 있는 기사들을 보여주는 '기사 화면'이 있다.
로그인을 하면 북마크 할 수 있다.

이를 고려하면 구독자가 카테고리별로 탐색할 수 있는 많은 기사들이 언제든지 있을 것이다.

요약하면 앱은 사용자들에게 다음을 제공.

- 읽을 수 있는 기사들을 보여준다.
- 카테고리별로 기사들을 탐색
- 로그인하면 특정 기사를 북마크할 수 있음.
- 조건에 맞는다면, 프리미엄 서비스에 접근 가능

다음 장들은 이 예제에 udf 이론을 도입

이 이론들이 문제들을 어떻게 UI레이어 앱 아키텍처가 해결하는지 설명

# [UI layer architecture](https://developer.android.com/topic/architecture/ui-layer#architecture)

UI는 액티비티나 프레그먼트와 같은 데이터를 표시하는 UI 요소를 참조. UI가 어떤 API를(뷰 or 컴포즈) 참조 하는지에서 독립적.

data layer에서 앱의 데이터를 관리 제공하기 때문에
UI 레이어는 다음 단계를 수행해야 함.

1. 앱 데이터를 소비. UI가 쉽게 그릴 수 있는 데이터로 변환
2. UI를 그릴 수 있는 데이터 소비. 사용자에게 보여줄 UI element에 대입.
3. 사용자의 이벤트를 받아 데이터에 반영

1~3을 반복한다.

다음은 UI 레이어를 어떻게 구현하는지 설명한다.

특히 다음 작업과 컨셉을 다룬다.

- UI state 정의 방법
- UDF로 UI state를 생성과 관리
- UDF 이론을 따라 관찰할 수 있는 데이터로 UI state를 노출시키는 방법
- 관찰할 수 있는 UI state를 소비하는 UI를 구현하는 방법
- UI state의 정의가 가장 근본

# [Define UI state](https://developer.android.com/topic/architecture/ui-layer#define-ui-state)

UI는 기사들과 각 기사들의 메타데이터를 보여준다.
앱이 사용자에게 보여주는 이 정보는 UI state 이다.

다시 말해 UI가 사용자가 보는 것이라면, UI state는 앱이 그들이 봐야 하는 것을 말하는 것이다.

양면이 같은 코인과 같다. UI는 UI state의 시각적 표시 이다.
UI state에 어떤 변화도 즉시 UI에 반영되어야 한다.

<img scr ="./documents/mad-arch-ui-elements-state.png" width="500"/>

뉴스 앱의 요구사항을 충족하기 위해, 정보는 전체를 그릴 UI가 캡슐화 될 수 있는 NewsUiState data class에 다음과 같이 정의.

```
data class NewsUiState(
    val isSignedIn: Boolean = false,
    val isPremium: Boolean = false,
    val newsItems: List<NewsItemUiState> = listOf(),
    val userMessages: List<Message> = listOf()
)

data class NewsItemUiState(
    val title: String,
    val body: String,
    val bookmarked: Boolean = false,
    ...
)
```

# [Immutability](https://developer.android.com/topic/architecture/ui-layer#state-immutability)

UI state는 불변이다.

불변 객체는 특정 시점에 앱에 상태를 보장해준다.
UI가 하나의 역할에만 집중할 수 있게 해줌 : 상태를 읽고 UI 요소에 업데이트.
이로인해 UI state는 데이터 자체 수정을 하면 안된다.
데이터의 일관성을 해치는 single sources of truth 이론을 위반 하는 것이됨.

예를들어
만약 북마크가 액티비티에서 업데이트 되었다고 가정
기사의 북마크 상태의 소스인 데이터 레이어에 반영이 되어야 함.
불변 데이터 클래스는 안티패턴을 예방하는데 매우 유용하다.

Key Point: 데이터 소스의 오너만이 노출해야 하는 데이터를 업데이트 하는 책임을 갖는다.

# [Naming conventions in this guide](https://developer.android.com/topic/architecture/ui-layer#naming-conventions)

functionality + UiState.

# [Manage state with Unidirectional Data Flow](https://developer.android.com/topic/architecture/ui-layer#udf)

이전에 불변의 데이터에 대해 알아봤다. 하지만 실제 앱에서 데이터는 시간이 지남에 따라 역동적으로 변함.
사용자 상호작용 또는 다른 이벤트로 데이터 앱의 근본적 데이터 수정이 필요할 수 있다.

중재자가 이 상호작용을 처리하게 하면 유용할 수 있다.
각 이벤트에 적용할 로직들을 정의하고, UI state를 생성을 위해 백업 데이터 소스에 정교한 변환을 수행
이 상호작용과 로직은 UI 자체에 담을 수 있다.
하지만 이건 UI가 가지고 있는 이름에서 명시하는 것보다 커지기 시작하면 다루기 어려워 질 수 있다.
데이터 주인, 생산자, 변환자 등의 역할을 하게 될 수 있음.
나아가 결과 코드가 다른 경계과 강하게 결합되어 테스트에도 영향을 줄 수 있음.
결과적으로 매우 단순하지 않은 이상 UI안에 이런 기능을 구현하면 안됨.
UI는 UI state를 소비하여 그리는 일에 대한 역할이 최우선. (다른일을 시킬 수 있지만 조금이라도 그리는데 영향을 주면 기능을 분리 해야함.)

이 장은 UDF에 대해 논의함으로써, 아키텍처 패턴이 건강하게 책임을 분리하는 방법을 배운다.

# [State holders](https://developer.android.com/topic/architecture/ui-layer#state-holders)

UI state를 생성하고 필요한 로직을 담고 있는 클래스를 state holder라 부른다.
state holder는 관리할 UI 요소에 따라 (하단 바와 같은 싱글 위젯 부터 전체 화면 또는 네비게이션 목적지 까지) 다양한 크기가 된다.

전형적인 구현은 ViewModel 이다. 애플리케이션에 따라 간단한 클래스로도 가능.
News app 에서는 NewsViewModel을 state holder로 사용

Key Point: 뷰모델은 '화면단 UI state' 와 'data layer 접근' 관리를 위한 권장 구현 방법.
configuration 변경에도 데이터가 생존.

뷰모델은 앱의 이벤트를 적용할 로직을 정의하고 업데이트 된 state를 결과로 생성.

UI와 state 생산자 사이 상호의존을 만드는데는 많은 방법이 있다.
UI와 뷰모델 사이 상호작용은 이벤트 입력과 state 출력을 보장하는데 대부분 사용할 수 있다.

이 관계는 다음 다이어그램에서 확인 할 수 있다.

<img src = "/documents/mad-arch-ui-udf.png" width="500" />

상태가 내려가고 이벤트가 올라가는 패턴을 UDF 라고 부른다.

앱 아키텍처를 위한 이 패턴의 결과는 다음과 같다.

- 뷰모델은 UI로 부터 소비될 state를 가지고 노출한다. viewmodel은 앱 데이터를 UI state로 변환한다.
- UI는 뷰모델에 사용자 이벤트를 알린다.
- 뷰모델은 사용자 액션과 상태 업데이트를 다룬다.
- 업데이트된 상태는 UI를 그리는데 피드백 된다.
- 상태의 변화를 야기하는 어던 이벤트라면 위 단계를 반복한다.

목적지와 화면 탐색하는 동안, 뷰모델은 저장소 또는 usecase에서 데이터를 얻고 (상태의 변환을 야기하는 이벤트의 이펙트를 포함시키는 동안) UI state로 변환한다.  

사용자가 북마크를 요청한다면, 상태 변환을 야기하는 이벤트가 발생 한 것.
상태 생산자로서, 모든 UI state에 모든 필드에 배포하기위한 필요한 로직을 정의하는 것은 뷰모델의 역할이다. 그리고 UI 전체를 그리는데 필요한 이벤트를 처리한다.

<img scr = "/documents/mad-arch-ui-udf-in-action.png" width="500" />

다음 섹션에서는 상태 변화를 야기하는 이벤트와 어떻게 UDF를 사용에 이를 처리하는지 알아본다.

# [Types of logic](https://developer.android.com/topic/architecture/ui-layer#logic-types)

북마크를 하는것은 앱에 값을 주기 때문에 비지니스 로직이다.

정의하는데 중요한 다른 로직의 종류가 있다.

비지니스 로직은 앱 데이터를 위한 제품 요구사항을 구현한 것이다. 북마크가 비지니스 로직 중 하나.
비지니스 로직은 일반적으로 도메인 또는 데이터 레이어에 위치한다. UI 레이어에는 결코 있으면 안됨.

UI 행동 로직 또는 UI 로직은 상태 변화를 어떻게 화면에 표시 하는지에 관한 것이다. 
예를들어 안드로이드 리소스를 사용하여 올바른 텍스트를 포함 시키는 것과 버튼 클릭 시 특정 화면으로 이동시키는 것
또는 토스트로 사용자 메세지를 화면에 표시해주는 것 등 이다.

UI 로직은 특히 Context와 같은 UI 타입을 포함할 때 뷰모델이 아닌 UI에 위치해야 한다.
만약 UI가 복잡해지고 테스트와 관심사의 분리를 위해 UI로직을 다른 클래스에 위임하고 싶다면,
단순한 state holder 클래스를 생성할 수 있다.

안드로이드 SDK 의존성을 사용해 UI의 라이프사이클을 따르는 단순한 클래스를 UI 안에 만들 수 있다.
뷰모델 객체는 더욱 긴 수명을 갖는다.
state holder에 대한 더 많은 정보와 어떻게 이들이 UI 빌드를 돕는 context에 알맞는지 알기위해 Jetpack Compose State guide 확인.

# [Why use UDF?](https://developer.android.com/topic/architecture/ui-layer#why-use-udf)

UDF 상태 생성의 순환을 설계 한다. 또한 상태를 변경 발생 지점, 상태를 변형 하는 곳, 최종적으로 상태를 소비하는 곳을 분리 한다. 
이 분리는 UI가 정확하게 이름이 암시하는것을 할 수 있게 해준다. 상태 변화 감지에 따른 정보 표시, 해당 변경 사항을 뷰모델에 전달하여 사용자의 의도를 전달.

UDF는 다음을 할 수 있게 한다.

데이터 일관성. UI를 위한 single source of truth 
테스트 유용성. 소스의 상태는 고립되어 있어 UI와 독립적으로 테스트 가능.
유지보수성. 상태의 변형은 사용자의 이벤트와 데이터 소스가 변경 둘로 부터 결과를 발생하는 잘 정의된 패턴을 따른다.

# [Expose UI state](https://developer.android.com/topic/architecture/ui-layer#expose-ui-state)

UI state를 정의 했다면, 어떻게 state의 생성을 관리 하는지 알아본다.
그다음은 생성한 state를 UI에 제공하는 방법을 알아본다.

state 생성 관리에  UDF를 사용하기 때문에, 생성된 state를 (시간이 지남에 따라 여러 버전의 상태로 생성되는) stream-in 하는것을 고려해야한다.
그 결과 UI state는 LiveData 또는 StateFlow와 같은 관찰 가능한 데이터 홀더를 통해 노출해야 한다.

그 결과 UI는 ViewModel에서 직접 수동으로 데이터를 가져오지 않아도 어떠한 변화에도 반응 할 수 있다.
이는 항상 최신 버전의 UI state를 (환경 설정이 변경된 후 라도 빠르게 복구하는데 유용한) 캐시하는 이점을 갖기도 한다.

```
class NewsViewModel(...) : ViewModel() {

    val uiState: NewsUiState = …
}
```

Note: 잿팩 컴포즈 앱에서 UI stete를 노출하는데 mutableStateOf나 snapshotFlow 와 같은 관찰 가능 API를 사용할 수 있다.
적절한 확장 함수를 사용해서 StateFlow나 LiveData와 같은 관찰 가능 데이터 홀더들을 쉽게 컴포즈에서 소비할 수 있다.

데이터 노출은 상대적으로 간단하다. 

상태의 방출과 화면과 UI에 연결시키는 관계를 실어나르기 때문에, UI state 타입으로 데이터를 감쌀 필요가 있다.

나아가 UI 요소는 복잡해 진다면, 쉽게 UI state에 정의를 추가해, 추가 정보를 UI 요소에서 그릴 수 있게 한다.

UIstate 의 스트림을 생성하는 일반적인 방법은 뷰모델에서 변화가능한(MutableStateFlow<UiState>) 데이터를 가지고 있고 불변의 데이터(StateFlow<UiState>)를 노출하는 것이다.

```
class NewsViewModel(...) : ViewModel() {

    var uiState by mutableStateOf(NewsUiState())
        private set

    ...
}
```

뷰모델은 내부적으로 상태를 업데이트 할 수 있는 함수를 노출시켜 UI를 업데이트 할 수 있게 한다.

비동기 동작에 대한 예로 코루틴을 viewModelScope를 사용하여 실행할 수 있고, 동작이 끝나면 상태를 변경할 수 있다.

```
class NewsViewModel(
    private val repository: NewsRepository,
    ...
) : ViewModel() {

   var uiState by mutableStateOf(NewsUiState())
        private set

    private var fetchJob: Job? = null

    fun fetchArticles(category: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val newsItems = repository.newsItemsForCategory(category)
                uiState = uiState.copy(newsItems = newsItems)
            } catch (ioe: IOException) {
                // Handle the error and notify the UI when appropriate.
                val messages = getMessagesFromThrowable(ioe)
                uiState = uiState.copy(userMessages = messages)
            }
        }
    }
}
```

위 예제에선 NewsViewModel가 특정 카테고리에 대한 기사에 대한 패치를 시도 하고 결과를 성공 또는 실패를 UI가 적절하게 보여줘야 하는 시점에 UI state에 반영한다.

에러를 다루는 방법에 대해선 [다른장](https://developer.android.com/topic/architecture/ui-layer#show-errors)에서 알아본다.

# [Additional considerations](https://developer.android.com/topic/architecture/ui-layer#additional-considerations)

추가로 UI state 노출에 다음을 고려한다.

UI state 객체는 서로 관계 있는 상태를 다룬다.
이는 적은 불일치를 야기하고 코드를 쉽게 이해할 수 있게 한다.

만약 뉴스 기사 리스트와 북마크 항목 리스트를 서로 다른 스트림으로 관리한다면,
하나가 업데이트 되면 다른 하나는 업데이트가 안될 수 도 있다.
하나의 스트림으로 사용하면, 두 요소는 함께 유지된다.

나아가, 어떤 비지니스 로직은 소스들을 합쳐야 할 수 도 있다.
예를들어 로그인한 사용자만 북마크 버튼을 노출해야 하거나 구독한 사람만 프리미엄 기사들을 보여주는 경우이다.
이는 다음과 같이 정의 할 수 있다.

```
data class NewsUiState(
    val isSignedIn: Boolean = false,
    val isPremium: Boolean = false,
    val newsItems: List<NewsItemUiState> = listOf()
)

val NewsUiState.canBookmarkNews: Boolean get() = isSignedIn && isPremium
```

이 정의는 2개의 다른 속성으로 북마크 표시여부를 결정 한다.
비지니스 로지은 더 복잡하다. 모든 속성들을 즉시 사용 가능하게 하는 하나의 UIState에 갖게 하는것은 더욱 중요해지고 있다.  

UI state를 하나의 스트림 또는 다중 스트림?

항목들을 방출해야하는 관계에 달려있다.

하나의 스트림으로 노출하는 것에 이점은 편리하고 데이터 일관성을 유지 할 수 있다.
항상 최신 정보를 유지 할 수 있다. 

하지만 스트림을 나누는게 적합한 경우가 있다.
관계 없는 데이터 타입: 하나의 UI에서도 어떤 상태들은 서로가 완전히 독립 정보일 수 있다.

이 경우 별개의 상태를 하나로 묶는데 비용이 더 들 수 있다.
특히 데이터 하나의 상태가 다른 상태보다 잦은 업데이트를 할 경우.

UiState diffing: 더 많은 필드들이 UIState 객체에 있다면, 필드가 업데이트 되어 더 많은 방출이 일어날 것이다.
같은 값을 계속 방출되는 값에 대한 뷰는 diffing 매커니즘이 없기 때문에 모든 방출은 뷰 업데이트를 야기함.
이는 distinctUntilChanged와 같은 함수 사용 필요.

# [Consume UI state](https://developer.android.com/topic/architecture/ui-layer#consume-ui-state)

UI에서 UIState 객체의 소비를 위해 탐색 가능 데이터 타입에 종료 연산자를 사용.

예를들어 LiveData는 observe() 함수가 있다. Kotlin flows 에는 collect() 함수 가 있다.
탐색 가능 데이터 홀더를 UI에서 소비할 때, UI의 라이프사이클을 고려해야 한다.
UI가 표시되지 않을 때 탐색하지 않는 것은 중요함.
LiveData 사용 시 LifecycleOwner은 lifecycle을 관리 할 수 있다. flow 사용 시 코루틴 스코프와 repeatOnLifecycle API
사용하여 다룰 수 있다.

```
@Composable
fun LatestNewsScreen(
    viewModel: NewsViewModel = viewModel()
) {
    // Show UI elements based on the viewModel.uiState
}
```

Note: 예제에서 사용되는 StateFlow 객체는 활성 collectors가 없다면 작업을 멈추면 안된다.
하지만 flows 사용 시 어떻게 실행되는지 모를 수 있다. lifecycle-aware flow collection 사용은 
이런 변화를 재방문 다운 스트림 수집 코드 없이 ViewModel flow에 나중에 만들게 해준다.

# [Show in-progress operations](https://developer.android.com/topic/architecture/ui-layer#show-operations)

UIState 클래스에 boolean 필드로 로딩 상태를 나타낼 수 있다.
```
data class NewsUiState(
    val isFetchingArticles: Boolean = false,
    ...
)
```


```
@Composable
fun LatestNewsScreen(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = viewModel()
) {
    Box(modifier.fillMaxSize()) {

        if (viewModel.uiState.isFetchingArticles) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        // Add other UI elements. For example, the list.
    }
}
```

# [Show errors on the screen](https://developer.android.com/topic/architecture/ui-layer#show-errors)

에러를 보여주는 것은 프로그레스 처리방법과 유사하다.
둘다 boolean으로 처리 할 수 있다. 하지만 에러는 메세지를 포함할 수 있다.
또는 실패 재시도 등의 동작을 연결 할 수 도 있다.

에러 상태는 에러의 맥락을 적절하게 데이터 클래스 설계를 필요로 할 수 있다.

기사 패치중 프로그레스 바 표시, 그리고 결과가 에러라면 어떤 에러인지 표시 할 수 있다.

```
data class Message(val id: Long, val message: String)

data class NewsUiState(
    val userMessages: List<Message> = listOf(),
    ...
)
```

# [Threading and concurrency](https://developer.android.com/topic/architecture/ui-layer#threading-concurrency)
# [Navigation](https://developer.android.com/topic/architecture/ui-layer#navigation)
# [Paging](https://developer.android.com/topic/architecture/ui-layer#paging)
# [Animations](https://developer.android.com/topic/architecture/ui-layer#animations)
# [Samples](https://developer.android.com/topic/architecture/ui-layer#samples)
