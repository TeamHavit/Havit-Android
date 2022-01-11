# Havit-Android 🤳

콘텐츠를 저장하는 가장 쉬운 방법, Want it? Save it, Have it!

![image](https://user-images.githubusercontent.com/48551119/148033420-2eb6cda6-1de5-4cc6-9333-2a8363ca3d4a.png)


## 역할 💡
- **효림** : SaveActivity(Url 저장 프로세스) + Push Notification(FCM)
- **유빈** : HomeFragment(메인 뷰) 작업 
- **혜인** : CategoryFragment(카테고리 선택 및 수정), ContentsFragment(컨텐츠 리스트) 및 파생 다이얼로그
- **나영** : WebviewFragment(인앱브라우저), 글 검색, FIB(Url 수동 추가) 소셜 로그인


## 기술 스택 💻
- DataBinding
- LiveData
- MVVM
- Coroutine
- Util Class : Toast, Log, Dialog
- Navigation
- Base Fragment/Activity/(ViewModel)
- Push 알림 -> FCM

## 폴더링 구조 📂
- data(remote, local) → 서버, 데이터
- domain(entity) → 모델
- ui(activity, Fragment) → 뷰(Home, Category, MyPage....)
- util → BindingAdapter, 확장 함수, Dialog

### ex)
```
📂data
┗ 📂api
📂domain
┗ 📂entity
📂ui
┣ 📂base
┣ 📂category
┣ 📂home
┣ 📂mypage
┣ 📂onboarding
┣ 📂search
┣ 📂share
┣ 📂splash
┗📂webview
📂util
```
