export class RegisterRequest {
  constructor(
    public username: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public phone: string,
    public role: string,
  ) {
  }
}
