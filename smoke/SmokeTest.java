import com.velix.sdk.VelixClient;
import com.velix.sdk.models.*;

public class SmokeTest {
    static final String IMG = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=";

    static void result(String step, boolean ok, String detail) {
        System.out.println("RESULT:java:" + step + ":" + (ok ? "PASS" : "FAIL") + ":" + detail);
    }

    static boolean reachable(String msg) {
        String m = msg.toLowerCase();
        return !(m.contains("route not found") || m.contains("no route") || m.contains("401") || m.contains("403"));
    }

    public static void main(String[] args) {
        VelixClient client = VelixClient.builder()
                .apiUrl(System.getenv("API_BASE_URL"))
                .apiKey(System.getenv("VELIX_API_KEY"))
                .build();

        String personId = null;
        try {
            OnboardingResponse r = client.onboarding().enroll(
                    OnboardingRequest.builder().name("Smoke Test Java").frames(java.util.List.of(IMG, IMG, IMG)).build());
            personId = r.personId();
            result("onboarding", personId != null, "person_id=" + personId);
        } catch (Exception e) {
            result("onboarding", false, e.getMessage());
        }

        try {
            CheckinIdentifyResponse r = client.checkin().identify(
                    CheckinIdentifyRequest.builder().imageBase64(IMG).build());
            result("checkin", true, "matched=" + r.matched());
        } catch (Exception e) {
            result("checkin", false, e.getMessage());
        }

        if (personId != null) {
            try {
                client.lgpd().requestDeletion(personId);
                result("lgpd", true, "deletion-request ok");
            } catch (Exception e) {
                result("lgpd", false, e.getMessage());
            }
            try {
                client.me().get(personId);
                result("me", true, "got response");
            } catch (Exception e) {
                result("me", false, e.getMessage());
            }
        }

        String dummy = "00000000-0000-0000-0000-000000000000";
        try {
            client.events().createGuest(dummy,
                    CreateGuestRequest.builder().name("Guest Smoke").email("guest@smoke.test").build());
            result("events_create", true, "endpoint reachable");
        } catch (Exception e) {
            result("events_create", reachable(e.getMessage()), e.getMessage());
        }

        try {
            client.events().getGuest(dummy, dummy);
            result("events_get", true, "endpoint reachable");
        } catch (Exception e) {
            result("events_get", reachable(e.getMessage()), e.getMessage());
        }
    }
}
