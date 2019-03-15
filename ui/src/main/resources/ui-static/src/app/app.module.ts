import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {JwtModule} from '@auth0/angular-jwt';
import {OauthService} from './oauth.service';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,

    JwtModule.forRoot({
      config: {
        tokenGetter: OauthService.token,
        whitelistedDomains: ['localhost:9999']
      }
    })
  ],
  providers: [OauthService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
