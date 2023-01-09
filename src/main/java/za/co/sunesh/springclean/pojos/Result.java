package za.co.sunesh.springclean.pojos;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private boolean isValid;
    private List<String> errorMessages;

    public boolean isValid() {
        return this.errorMessages == null ? true : false;
    }

    public void appendError(String errorMessage) {
        if (this.errorMessages == null) this.errorMessages = new ArrayList<>();
        this.errorMessages.add(errorMessage);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
