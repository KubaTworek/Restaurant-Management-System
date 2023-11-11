package pl.jakubtworek.order;

class Cook {
    private Long cookId;

    Cook(final Long cookId) {
        this.cookId = cookId;
    }

    Long getCookId() {
        return cookId;
    }
}
