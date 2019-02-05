import {Routes} from '@angular/router';
import {BaseComponent} from '../views/base/base.component';
import {HomeComponent} from '../views/home/home.component';
import {ErrorComponent} from '../views/error/error.component';
import {TrendingComponent} from '../views/trending/trending.component';
import {VideoComponent} from '../views/video/video.component';
import {ProfileComponent} from '../views/profile/profile.component';
import {SettingsComponent} from '../views/settings/settings.component';
import {UploadComponent} from '../views/upload/upload.component';
import {SearchComponent} from '../views/search/search.component';
import {SubscriptionsComponent} from '../views/subscriptions/subscriptions.component';
import {HistoryComponent} from '../views/history/history.component';
import {GroupComponent} from "../views/group/group.component";

export const routes: Routes = [
  {
    path: '', component: BaseComponent, children: [
      {path: '', component: HomeComponent},
      {path: 'search/:page/:search_key', component: SearchComponent},
      {path: 'trending', component: TrendingComponent},
      {path: 'history', component: HistoryComponent},
      {path: 'subscriptions', component: SubscriptionsComponent},
      {path: 'settings', component: SettingsComponent},
      {path: 'upload', component: UploadComponent},
      {path: 'video/:id', component: VideoComponent},
      {path: 'group/:id', component: GroupComponent},
      {path: 'profile/:id', component: ProfileComponent},
      {path: '**', component: ErrorComponent}
    ]
  }
];
