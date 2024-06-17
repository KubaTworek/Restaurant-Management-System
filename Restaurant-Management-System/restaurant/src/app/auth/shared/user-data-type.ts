export class UserData {
  constructor(
    public id: string,
    public username: string,
    public firstName: string,
    public lastName: string,
    public role: string,
    public token: string,
    public refreshToken: string,
    public tokenExpirationDate: number,
    public refreshTokenExpirationDate: number,
  ) {
  }
}
