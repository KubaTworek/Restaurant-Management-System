import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {catchError, tap} from "rxjs/operators";
import {Observable, throwError} from "rxjs";
import {AuthorizationService} from "../auth/service/authorization.service";
import {RegisterRequest} from "../auth/shared/register-request.type";
import {LoginRequest} from "../auth/shared/login-request.type";
import {TranslateService} from "@ngx-translate/core";
import {OrderConfirmRequest} from "./dto/order-confirm-request.tye";
import {OrderReceiveRequest} from "./dto/order-receive-request.type";
import {OrderRequest} from "./dto/order-request.type";
import {OrderResponse} from "./dto/order-response.type";
import {MenuResponse} from "./dto/menu-response.type";

@Injectable({providedIn: 'root'})
export class DataStorageService {
  private apiUrl = 'http://localhost:9090';

  constructor(
    private http: HttpClient,
    private authorizationService: AuthorizationService,
    private translateService: TranslateService,
  ) {
  }

  // ORDERS
  // POST
  createOrder(request: OrderRequest): Observable<OrderResponse> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/orders`;

    return this.http
      .post<OrderResponse>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError(this.handleHttpError)
      );
  }

  confirmOrder(request: OrderConfirmRequest): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/orders/confirm`;

    this.http
      .post<OrderResponse>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError(this.handleHttpError)
      )
      .subscribe();
  }

  receiveOrder(request: OrderReceiveRequest): void {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/orders/receive`;

    this.http
      .post<OrderResponse>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError(this.handleHttpError)
      )
      .subscribe();
  }

  // GET
  fetchOngoingOrders(): Observable<OrderResponse[]> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/orders/ongoing`;

    return this.http
      .get<OrderResponse[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
      );
  }

  fetchAllOrders(): Observable<OrderResponse[]> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/orders`;

    return this.http
      .get<OrderResponse[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
      );
  }

  fetchOrder(orderId: string): Observable<OrderResponse> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/orders/${orderId}`;

    return this.http
      .get<OrderResponse>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError),
      );
  }

  // MENU
  // GET
  fetchMenus(): Observable<MenuResponse[]> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/menu-items`;

    return this.http
      .get<MenuResponse[]>(endpoint, {headers})
      .pipe(
        catchError(this.handleHttpError)
      );
  }

  // AUTH
  login(request: LoginRequest): Observable<any> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/users/login`;

    return this.http.post<any>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError((error) => {
          if (error.status === 404) {
            const errorMessage = this.translateService.instant('USERNAME_NOT_EXISTS');
            this.authorizationService.handleLoginError(errorMessage);
          } else if (error.status === 401) {
            const errorMessage = this.translateService.instant('INVALID_CREDENTIALS');
            this.authorizationService.handleLoginError(errorMessage);
          } else {
            console.error(error);
          }
          return throwError(error);
        }),
        tap(userData => this.authorizationService.handleLogin(userData))
      );
  }

  register(request: RegisterRequest): Observable<void> {
    const headers = this.createHeaders();
    const endpoint = `${this.apiUrl}/users/register`;

    return this.http.post<void>(endpoint, JSON.stringify(request), {headers})
      .pipe(
        catchError((error) => {
          if (error.status === 400) {
            const errorMessage = this.translateService.instant('USERNAME_ALREADY_EXISTS');
            this.authorizationService.handleRegisterError(errorMessage);
          } else {
            console.error(error);
          }
          return throwError(error);
        }),
        tap(() => this.authorizationService.handleRegister())
      );
  }

  refreshToken(): void {
    const headers = this.createHeadersForRefresh()
    const endpoint = `${this.apiUrl}/users/refresh-token`;

    this.http.post<any>(endpoint, null, {headers})
      .pipe(
        catchError((error) => {
          console.log('csc')
          return throwError(error);
        }),
        tap(userData => {
          this.authorizationService.handleRefresh(userData)
        })
      )
      .subscribe();
  }

  private createHeaders(): HttpHeaders {
    const token = this.authorizationService.getToken();

    return new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
      'Authorization': token || ''
    });
  }

  private createHeadersForRefresh(): HttpHeaders {
    const token = this.authorizationService.getRefreshToken()

    return new HttpHeaders({
      Accept: 'application/json',
      'Content-Type': 'application/json',
      'Authorization': token || ''
    });
  }

  private handleHttpError(error: HttpErrorResponse): Observable<never> {
    console.error(error);
    return throwError(error);
  }
}
