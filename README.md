# 오픈소스 프로젝트

## 보고서 목차는 다음과 같습니다.
. 제목
. 주제
. 기존(참고) 소스 주소
. 기존 소스의 라이센스
. 기존 소스의 기능 프로그램 흐름도(System Flow-chart)
. 목록(Function List)
. 클래스 목록(Class List) 및 분석
. 화면 구성 및 설명
. 내  소스 라이센스
. 기존 소스와 차별점
. 깃 허브 로그 정보
. 프로젝트의 특징
. 느낀점

### <제목>
오픈소스SW프로젝트의 제목은 Baby Sleep Sounds입니다.
이는 “아기 수면 소리” 라는 뜻으로, 기존의 어플리케이션 이름과 동일합니다. 기존의 오픈소스와 같이 아기들을 위한 수면 소리 제공을 목표로 하는 어플리케이션에 대해 프로젝트를 진행하였습니다.

### <프로젝트 주제>
Baby Sleep Sounds는 아기의 숙면을 위한 수면소리 어플리케이션입니다.
프로젝트 주제는 기존의 오픈소스 어플리케이션에서 사용자가 요구할 만한 기능들을 분석하고 추가하는 것을 목표로 했습니다.

이전과 차별화된 기능, 성능에 대한 구현을 목표로 했습니다.

### <기존(참고) 소스 주소>
## https://github.com/brarcher/baby-sleep-sounds
## ![image](https://github.com/NohGyuSeon/baby-sleep-sounds/assets/97013643/24c69bd6-7e58-44f2-930a-c870210c4a97)

기존의 소스 주소입니다. GitHub 사이트에 올라와있는 오픈소스를 프로젝트로 선정하였습니다.

해당 프로젝트는 안드로이드 스튜디오 Tool로 진행하였습니다. 언어는 Java를 사용하였고, GitHub과 연동하여 Commit, Push 진행하였습니다.

### <기존 소스의 라이센스>
해당 프로젝트는 오픈소스로 Github에 소스가 공개되어 있으며, 구글 playstore에 정식 등록되어있습니다.

라이선스는 GNU General Public License v3.0를 따르고 있습니다
## ![image](https://github.com/NohGyuSeon/baby-sleep-sounds/assets/97013643/3be6ffb5-3106-4414-8a03-c23403301455)

### <기존 소스의 기능 프로그램 흐름도(SYSTEM FLOW-CHART>
해당 프로젝트의 기능 프로그램 흐름도에 대해 개략적으로 살펴보겠습니다.

기존 프로젝트에서는 앱이 시작되면, 스플래시 액티비티를 거치지 않고 곧바로 메인 액티비티가 보여집니다. 이후에 화면 설명에서 보여지겠지만 메인 화면에서 앱의 모든 기능들을 실행할 수 있도록 간단하게 화면이 설계되어있습니다.

사용자가 수면소리(사운드)를 설정하면 해당하는 사운드가 재생됩니다. 이때, 소리가 재생중이면 소리를 멈추고, 소리가 재생중이 아니라면 재생하게 됩니다. 또한 해당 메소드에서 타이머는 함께 작동하며, 사운드가 중지되면 타이머도 같이 중지되었다가, 다시 재생되면 resume되는 흐름으로 진행됩니다. 

전체적인 앱의 동작은 안드로이드에서 액티비티의 생명주기와 동일하게 onCreate(), onStart(), onResume()이 차례로 동작하며 실행됩니다.
이후 직접 실습한 프로젝트에서는 여러 액티비티가 만들어져있는데, 화면 전환이 일어날 때, 기존의 액티비티는 onPasue()가 되어지고 onStop()이 이루어지며, onDestroy()를 통해 액티비티 종료가 됩니다.
onStop() 상태에서 기존 액티비티로 전환하여 보여질 때는 onRestart()를 통해서 onStart() 상태가 되어집니다.

또한 수면소리는 사용자가 앱의 화면에서 벗어나, 메신저나 화면이 꺼져있을 때(잠을 자고 있을 때) 등의 상황에서도 계속 재생되어져야 하기 때문에, 이를 위한 서비스가 구현되어있습니다. 서비스는 액티비티, 브로드캐스트, 콘텐트프로바이더와 함께 안드로이드 4대 주요 컴포넌트입니다.

### <목록(Function List)>
기존 프로젝트의 기능 목록으로는 크게 5가지이며 다음과 같습니다.
@ 사용자가 선택한 수면 소리를 재생할 수 있습니다.
@ 사용자가 선택한 시간에 맞추어 타이머를 작동할 수 있습니다.
@ 로우 패스 필터(Low Pass Filter)를 통해 사운드 필터를 적용할 수 있습니다.
@ 다크모드 테마를 적용할 수 있습니다.
@ 정보 탭을 통해서 개발자 정보 및 라이센스를 확인할 수 있습니다.

### <클래스 목록(Class List) 및 분석>
실습한 프로젝트의 클래스는 총 8개이며, 빨간색으로 표시된 것들이 직접 추가하거나, 수정한 프로젝트입니다.
AudioService
LoopingAudioPlayer
MainActivity
Preferences
SettingsActivity
SplashActivity
VideoActivity
WebActivity

메인 액티비티는 타이머 기능 시각화 및 다른 액티비티들과의 상호 작용을 위해서 주로 수정하였고, 스플래시 화면 추가를 위한 스플래시 액티비티, 비디오 영상을 보여주기 위한 비디오 액티비티, 개발자 페이지를 위한 웹 액티비티를 추가하였습니다.

기존의 앱을 분석하는 과정에서 매인 액티비티에 대한 분석이 가장 많은 시간을 소모하였습니다. 오디오 서비스나 설정 액티비티와 관련된 부분에서는 직접 수정하거나, 수정할 수 없는 부분들이 많았으며 대부분의 추가할 기능들은 메인 액티비티에 연관되어 있었습니다.

ImmutableMap 객체로써 타이머 시간들이 하나씩 설정되어 있었고, 시크바에 사운드나, 타이머 시간이 설정되어있는 형태였습니다. 단순한 디자인과 화면 구성이 좋을 수 있지만, 저는 제가 직접 사용할 수준의 앱을 만들고 싶었고, 이를 위해 디자인과 기능을 추가하기로 했습니다. 뒤에서 차이점을 기술하면서 자세히 살펴보겠지만 액티비티 뿐만 아니라 메뉴 탭 수정을 위한 menu.xml 파일과 한국어 패치를 위한 strings.xml를 수정, 그리고 다양한 오픈소스 라이브러리를 추가하기 위해 gradle에 직접 라이브러리를 추가하고 sync하였습니다. 메니페스트 파일을 통해서 권한 추가가 이루어졌으며, 유튜브 라이브러리 사용을 위해 lib에 해당 jar_files도 추가하였습니다. 기존의 앱에서는 영어, 독일어, 불어를 지원하고 있었고 오픈된 mp3 사운드를 가져와서 출처를 밝히며 사용하고 있었습니다.

특별한 애니메이션 기능이 없었고 스플래시 화면도 없었기에 이를 구현하고자 하였고, 가장 중요하게 생각한 타이머에 대한 남은 시간을 직접 확인할 수 없어서 이를 구현하였습니다.

메인 액티비티에서 추가하거나 수정한 부분들에 대한 코드에는 대부분 주석을 달아서 작성하였습니다. 이는 보고서 뒷부분에 나오는 깃헙 로그에서 확인할 수 있겠습니다. 

### <화면 구성 및 설명>
화면 구성은 크게 5가지이며, 메인 액티비티는 주로 수정을 하고, 비디오 액티비티와 웹 액티비티는 직접 추가하였습니다.
![image](https://github.com/NohGyuSeon/baby-sleep-sounds/assets/97013643/c8b3dc15-91ff-4739-9fed-9a1d39aaed57)
![image](https://github.com/NohGyuSeon/baby-sleep-sounds/assets/97013643/a007bf67-3406-4642-8c17-25cb38d4da44)
![image](https://github.com/NohGyuSeon/baby-sleep-sounds/assets/97013643/322ab722-0fc9-40cf-9f66-74bddbc5c296)
![image](https://github.com/NohGyuSeon/baby-sleep-sounds/assets/97013643/c6a07491-e453-4381-bec5-26c9613b0796)
![image](https://github.com/NohGyuSeon/baby-sleep-sounds/assets/97013643/ff214fa8-3ce3-498e-9447-2c6e2c4fa93c)

앱 실행 시, 가장 먼저 스플래시 화면이 나타나며 여기 이미지들에는 없지만, 뒤에 기존 소스와 차별점에서 확인할 수 있습니다. 간단한 앱 아이콘과 함께 이름 및 개발자 이니셜이 보여지는데 오픈소스 라이브러리에서 애니메이션을 가져와서 적용하였습니다.

가장 첫 번째 이미지가 메인 액티비티이며, 기존의 화면에서 GIF 이미지를 구현하였고 메뉴 탭을 통해서 기존의 개발자 페이지와 직접 만든 피드백 페이지에 접근할 수 있습니다. 또한 타이머의 남은 시간을 동적으로 보여줄 수 있으며 타이머 아이콘 클릭 시 해당 타이머 뷰에 애니메이션을 적용합니다.
버튼들의 디자인과 토스트 메시지 또한 직접 작성한 드로어블, xml 파일 또는 오픈소스 라이브러리를 sync하여 수정하였으며 안드로이드 아이콘 클릭 시 두 번째 이미지에서 보여지는 것처럼 비디오 액티비티로 전환됩니다.

해당 비디오 액티비티에는 기존의 수면소리 앱이 아기들을 위한 것임에 초점을 맞추어, 보호자가 원할 때 유아용 애니메이션을 재생할 수 있도록 만들어졌습니다. 3개의 유튜브 영상을 가져왔으며, 안드로이드 아이콘 클릭 시 비디오가 업데이트되면서 재생, 중지 기능에 따라 작동할 수 있도록 합니다.

세 번째와 다섯 번째 이미지는 기존의 앱에 있던 화면이며, 메뉴 탭에서 톱니바퀴, 3개점 아이콘을 클릭하여 접근할 수 있습니다.

상단 메뉴 탭의 info 아이콘을 클릭하면 네 번째 이미지인 피드백 액티비티로 전환됩니다. WebView를 통해서 안드로이드 구글 플레이스토어에서 바로 피드백이 가능하도록 하였으며, 실습한 프로젝트는 아직 구글 플레이스토어에 출시된 것은 아니므로 기존의 앱에 대한 플레이스토어 화면을 설정하였습니다.
또한, 저에 대한 이메일과 깃헙 소스 링크를 AutoLink를 통해서 클릭 시 바로 접근할 수 있도록 하였습니다.

### <내 소스 깃허브 주소>
저의 소스가 업로드 되어있는 깃허브 주소는 다음과 같습니다.
https://github.com/NohGyuSeon/baby-sleep-sounds

### <내 소스 라이센스>
실습한 프로젝트의 라이선스도 기존의 프로젝트와 동일하게 
GNU General Public License v3.0를 따르고 있습니다.

### <기존 소스와 차별점>
기존 소스와의 차별점은 주로 8가지 기능에 대한 것이며, 다음과 같습니다.
@ GIF 이미지 구현 (사운드에 따라 메인 화면에 동적으로 보여줍니다)
@ 타이머 기능 수정 (타이머 동작에 따른 남은 시간을 메인 화면에 동적으로 보여줍니다)
@ 타이머 애니메이션 (타이머 아이콘 클릭 시 애니메이션을 발생시킵니다)
@ 스플래시화면 (앱 시작 시 스플래시화면을 보여줍니다)
@ 가로 화면 추가 (가로 화면에 대한 레이아웃을 추가합니다)
@ 비디오 영상 추가 (안드로이드 아이콘 클릭 시 유튜브에서 유아용 애니메이션 비디오를 보여줄 수 있는 화면이 나타납니다. 3개의 영상에 대해서 비디오를 동작시킬수 있습니다)
@ 한국어 패치 (앱에서 한국어 설정 시 한국어 앱을 사용할 수 있습니다)
@ 피드백 페이지 (사용자 피드백을 보다 쉽게 할 수 있도록 하는 화면을 추가합니다)

기존 프로젝트는 이전에 기술한 것과 같이 모든 기능들은 하나의 화면에서 구현할 수 있도록 되어있으며, 별도의 타이머 시각화나 스플래시 화면이 없습니다.

### <깃 허브 로그 정보>
커밋은 총 11번 진행하였으며, 한 번에 많은 수정보다는 우선순위를 정해놓고 차례대로 진행하였습니다. 1356 코드 라인 추가하였습니다.

<프로젝트의 특징>
프로젝트의 특징으로는 편의성, 실용성, 감성과 같이 3가지 측면이 있습니다.

● 편의성
- 사용자 편의성을 고려하여 타이머 기능을 시각화하여보여줍니다.
- 한국어 패치를 통해 사용자 편의성을 증가시켰습니다.
- 피드백 액티비티를 구현하여 사용자 요구사항을 보다 쉽게 전달할 수 있도록 하였습니다.

● 실용성
- 가로 화면을 추가하여 실용성을 더했습니다.
- 아기의 숙면을 위한 수면소리 어플리케이션인 만큼, 보호자가 필요 시 유아용 비디오 애니메이션를재생할 수 있도록 해당 액티비티를 구현하였습니다.

● 감성
- 화면 레이아웃, 뷰 레이아웃을 수정 및 추가하여 프로젝트의 전체적인 모양과 느낌을 만들었습니다.
- 전환, 애니메이션 및 동작을 추가하여 프로젝트의 개성을 더했습니다.

### <느낀점>
유튜브 영상을 어플리케이션에 추가하고, 광고 배너 기능을 통해 앱이 수익화 될 경우 저작권 문제가 발생할 수 있는 점을 파악했습니다. 이후 앱을 실제로 출시하게 된다면, 이러한 광고 배너 기능을 넣게 될 가능성이 클 것인데 다양한 저작권 문제에 대해서 잘 파악하고 라이센스 또한 잘 숙지해야 할 것임을 인지하였습니다.

타이머 기능에 직접 시간을 추가하려는 과정에서 ImmutableMap에 대한 기존 소스에 대한 수정이 쉽지 않았습니다. 기존의 소스 코드를 수정하는 것은 어떠한 기능을 새로 추가하는 것보다 훨씬 어려운 일이 될 수 있음을 인지하였습니다.

따라서 이미 만들어진 오픈소스를 활용함으로써 앱 개발에 더욱 빠르고 간편하게 접근할 수 있었지만, 기존 소스에서 원하는 기능을 마음대로 수정하는 것은 생각보다 어려운 것임을 인지하였습니다.

그럼에도 불구하고, 오픈소스SW프로젝트를 진행하면서 기존의 안드로이드 학습을 통해서만 배울 수 있었던 개념과는 다르게 굉장히 빠른 속도로 개발이라는 것의 본질에 접근할 수 있었습니다. 안드로이드를 공부할 때 학습했었던 본질적인 것들(가령, 액티비티의 생명주기)이 굉장히 중요하다는 것을 깨닫을 수 있었고, 사이드 프로젝트가 개발자에게 있어서 중요한 학습인 것을 몸소 느낄 수 있었습니다. 

또한 최근의 안드로이드 오픈소스는 대부분 코틀린으로 작성되어진 것을 파악하였고, 이에 대한 학습 필요성을 느꼈으며 시장에는 굉장히 많은 오픈소스 라이브러리가 있는 것을 파악하였습니다. 이후에도 더 나은 앱을 개발하고 수익화하는 경험을 쌓을 것이며, 이에 필요한 본질적인 학습과 사이드 프로젝트을 통해 결실을 맺을 것을 목표하였습니다.

감사합니다.

# Baby Sleep Sounds
[![Build Status](https://travis-ci.org/brarcher/baby-sleep-sounds.svg?branch=master)](https://travis-ci.org/brarcher/baby-sleep-sounds)

Play select sounds to help a baby sleep

<a href="https://f-droid.org/packages/protect.babysleepsounds/" target="_blank">
<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="90"/></a>
<a href="https://play.google.com/store/apps/details?id=protect.babysleepsounds.play" target="_blank">
<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" alt="Get it on Google Play" height="90"/></a>

This application includes several audio files which when selected will play in a loop until stopped.

## Screenshot

[<img src="https://user-images.githubusercontent.com/5264535/33806211-8cdd6fa4-dd92-11e7-9f2e-61d382519b3f.png" width=250>](https://user-images.githubusercontent.com/5264535/33806211-8cdd6fa4-dd92-11e7-9f2e-61d382519b3f.png)
[<img src="https://user-images.githubusercontent.com/5264535/33806212-8da8dfcc-dd92-11e7-8ab0-c2ea780f7939.png" width=250>](https://user-images.githubusercontent.com/5264535/33806212-8da8dfcc-dd92-11e7-8ab0-c2ea780f7939.png)
[<img src="https://user-images.githubusercontent.com/5264535/33806213-8ebbef94-dd92-11e7-8360-bd10b91b6ab4.png" width=250>](https://user-images.githubusercontent.com/5264535/33806213-8ebbef94-dd92-11e7-8360-bd10b91b6ab4.png)

## Building

To build, use the gradle wrapper scripts provided in the top level directory of the project. The following will
compile the application and run all unit tests:

GNU/Linux, OSX, UNIX:
```
./gradlew build
```

Windows:
```
./gradlew.bat build
```

## Thanks

App icon '[Music](https://thenounproject.com/term/music/886761/)' by Aleks from the Noun Project.

The following sound resources were created by [Canton Becker](http://whitenoise.cantonbecker.com)
and are distributed under the CC BY-NC-SA 3.0 License:
 - noise_only.mp3

The following sound resources were created by [MC2Method.org](http://mc2method.org/white-noise/)
and are distributed under the condition of "Personal Use":
  - dryer.mp3
  - fan.mp3
  - ocean.mp3
  - rain.mp3
  - refrigerator.mp3
  - shower.mp3
  - stream.mp3
  - vacuum.mp3
  - water.mp3
  - waterfall.mp3
  - waves.mp3

[Campfire-1.mp3](https://www.soundjay.com/nature/campfire-1.mp3) Copyright SoundJay.com Used with Permission.

# Note from Developer
The developer is currently on hiatus from the project. Questions and pull requests may not be looked at for some time. Apologies in advance.

# LICENSE
                     END OF TERMS AND CONDITIONS

            How to Apply These Terms to Your New Programs

  If you develop a new program, and you want it to be of the greatest
possible use to the public, the best way to achieve this is to make it
free software which everyone can redistribute and change under these terms.

  To do so, attach the following notices to the program.  It is safest
to attach them to the start of each source file to most effectively
state the exclusion of warranty; and each file should have at least
the "copyright" line and a pointer to where the full notice is found.

    {one line to give the program's name and a brief idea of what it does.}
    Copyright (C) 2023 NohGyuSeon

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

Also add information on how to contact you by electronic and paper mail.

  If the program does terminal interaction, make it output a short
notice like this when it starts in an interactive mode:

    {project}  Copyright (C) 2023 NohGyuSeon
    This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.
    This is free software, and you are welcome to redistribute it
    under certain conditions; type `show c' for details.

The hypothetical commands `show w' and `show c' should show the appropriate
parts of the General Public License.  Of course, your program's commands
might be different; for a GUI interface, you would use an "about box".

  You should also get your employer (if you work as a programmer) or school,
if any, to sign a "copyright disclaimer" for the program, if necessary.
For more information on this, and how to apply and follow the GNU GPL, see
<http://www.gnu.org/licenses/>.

  The GNU General Public License does not permit incorporating your program
into proprietary programs.  If your program is a subroutine library, you
may consider it more useful to permit linking proprietary applications with
the library.  If this is what you want to do, use the GNU Lesser General
Public License instead of this License.  But first, please read
<http://www.gnu.org/philosophy/why-not-lgpl.html>.

