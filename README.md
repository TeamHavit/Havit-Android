# Havit-Android 🤳


  
**기억하고 싶은 콘텐츠를 저장하는 가장 쉬운 방법, HAVIT을 소개합니다 :)**
  

https://user-images.githubusercontent.com/55099365/150919289-52d35f31-c658-433a-8ffa-d84c8e6e85d8.mp4




## 팀원 소개 및 역할 💡


| [@kxxhyorim](https://github.com/kxxhyorim)                   | [@yubinquitous](https://github.com/yubinquitous)             | [@sdu07024](https://github.com/sdu07024)                     | [@ny2060](https://github.com/ny2060)                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| ![image](https://user-images.githubusercontent.com/59546818/152497193-bb9d38ac-7b2e-420a-97be-f1341d9f4d31.png) | ![image](https://user-images.githubusercontent.com/59546818/152497219-690171c5-5f05-42c9-baf2-628efe657f09.png) | ![image](https://user-images.githubusercontent.com/59546818/152497241-fb52e7bb-5150-40db-be90-587b6dd92c7b.png) | ![image](https://user-images.githubusercontent.com/59546818/152497211-ac7fc3a1-79c0-4075-a3ac-30739bdae42f.png) |
| - **`URL 저장 프로세스`**<br />- **`푸시알림(Firebase Cloud Messaging)`** | - **`메인 뷰 (HomeFragment)`**<br />- **`알림설정 뷰`**      | - **`카테고리별 컨텐츠 뷰`**<br />- **`카테고리 수정 뷰`**<br /> | - **`컨텐츠 검색 뷰`**<br />- **`마이페이지 뷰`**<br />- **`Bottom Navigation + Floating Action Button`** |




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
