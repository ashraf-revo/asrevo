import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {HeaderComponent} from './componant/header/header.component';
import {NavComponent} from './componant/nav/nav.component';
import {VideoGridComponent} from './componant/video-grid/video-grid.component';
import {VideoRowComponent} from './componant/video-row/video-row.component';
import {AnimateBarComponent} from './componant/animate-bar/animate-bar.component';
import {NotificationComponent} from './componant/notification/notification.component';
import {VideoRowSearchComponent} from './componant/video-row-search/video-row-search.component';
import {CommentRowComponent} from './componant/comment-row/comment-row.component';
import {PlayerComponent} from './componant/player/player.component';
import {CommentsBoxComponent} from './componant/comments-box/comments-box.component';
import {UserRowComponent} from './componant/user-row/user-row.component';
import {BaseComponent} from './views/base/base.component';
import {HomeComponent} from './views/home/home.component';
import {ErrorComponent} from './views/error/error.component';
import {TrendingComponent} from './views/trending/trending.component';
import {VideoComponent} from './views/video/video.component';
import {ProfileComponent} from './views/profile/profile.component';
import {SettingsComponent} from './views/settings/settings.component';
import {UploadComponent} from './views/upload/upload.component';
import {SearchComponent} from './views/search/search.component';
import {SubscriptionsComponent} from './views/subscriptions/subscriptions.component';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {routes} from './services/routes';
import {TubeService} from './services/tube.service';
import {DefaultService} from './services/default.service';
import {UserService} from './services/user.service';
import {AuthService} from './services/auth.service';
import {FeedbackService} from './services/feedback.service';
import {RouterModule} from '@angular/router';
import {MomentModule} from 'angular2-moment';
import {FormsModule} from '@angular/forms';
import {HistoryComponent} from './views/history/history.component';
import {ErrorHandlersService} from "./services/error-handlers.service";
import {VideoRowHistoryComponent} from './componant/video-row-history/video-row-history.component';
import {GroupComponent} from './views/group/group.component';


@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    BaseComponent,
    HomeComponent,
    ErrorComponent,
    TrendingComponent,
    VideoComponent,
    ProfileComponent,
    SettingsComponent,
    UploadComponent,
    HeaderComponent,
    NavComponent,
    VideoGridComponent,
    VideoRowComponent,
    AnimateBarComponent,
    NotificationComponent,
    VideoRowSearchComponent,
    CommentRowComponent,
    PlayerComponent,
    CommentsBoxComponent,
    UserRowComponent,
    SubscriptionsComponent,
    HistoryComponent,
    VideoRowHistoryComponent,
    GroupComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MomentModule,
    FormsModule,
    RouterModule.forRoot(routes)
  ],
  providers: [DefaultService, TubeService, UserService, AuthService, UserService, FeedbackService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlersService,
      multi: true
    }

  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
