module com.Bookworm {
    requires javafx.controls;
	requires google.api.client;
	requires com.google.api.client;
	requires com.google.api.client.json.jackson2;
	requires google.api.services.books.v1.rev114;
	requires javafx.graphics;
	requires javafx.fxml;
    exports com.Bookworm;
	exports com.Bookworm.ui;
}