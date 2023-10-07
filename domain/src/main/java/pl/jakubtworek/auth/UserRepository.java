package pl.jakubtworek.auth;

interface UserRepository {
    User save(User entity);
}