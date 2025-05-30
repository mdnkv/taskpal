import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import {provideRouter, withComponentInputBinding, withHashLocation} from '@angular/router';
import {provideHttpClient, withInterceptors} from '@angular/common/http';

import {
  AutoRefreshTokenService,
  provideKeycloak,
  UserActivityService,
  withAutoRefreshToken,
  createInterceptorCondition,
  IncludeBearerTokenCondition,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  includeBearerTokenInterceptor
} from 'keycloak-angular';

import { routes } from './app.routes';
import {environment} from '../environments/environment';

const urlCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^(http:\/\/localhost:8000)(\/.*)?$/i,
  bearerPrefix: 'Bearer'
});

export const appConfig: ApplicationConfig = {
  providers: [
    provideKeycloak({
      config: {
        url: environment.keycloak.url,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId
      },
      initOptions: {
        onLoad: 'login-required'
      },
      features: [
        withAutoRefreshToken({
          onInactivityTimeout: 'login',
          sessionTimeout: 60000
        })
      ],
      providers: [AutoRefreshTokenService, UserActivityService]
    }),
    {
      provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
      useValue: [urlCondition]
    },
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding(), withHashLocation()),
    provideHttpClient(withInterceptors([includeBearerTokenInterceptor]))
  ]
};
