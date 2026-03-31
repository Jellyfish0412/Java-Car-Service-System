package customer_function;

public class Testfeedback {

    public static void main(String[] args) {

        FeedbackDAO feedbackDAO = new FeedbackDAO();

        System.out.println("===== TEST 1: Submit new feedback =====");
        boolean result1 = feedbackDAO.submitFeedback("A004", "C001", "5", "Great service!");
        System.out.println("Submit result: " + (result1 ? "SUCCESS" : "FAILED"));

        System.out.println("\n===== TEST 2: Submit duplicate (same appointmentID) =====");
        boolean result2 = feedbackDAO.submitFeedback("A001", "C001", "5", "Try again");
        System.out.println("Submit result: " + (result2 ? "SUCCESS" : "FAILED (expected)"));

        System.out.println("\n===== TEST 3: Get feedback by appointmentID =====");
        Feedback fb = feedbackDAO.getFeedbackByAppointmentID("A001");
        if (fb != null) {
            System.out.println("FeedbackID : " + fb.getFeedbackID());
            System.out.println("AppointmentID: " + fb.getAppointmentID());
            System.out.println("CustomerID : " + fb.getCustomerID());
            System.out.println("Comment    : " + fb.getComment());
            System.out.println("Rating     : " + fb.getRating());
            System.out.println("Date       : " + fb.getDate());
        } else {
            System.out.println("No feedback found.");
        }

        System.out.println("\n===== TEST 4: Get all feedbacks by customerID =====");
        java.util.List<Feedback> list = feedbackDAO.getFeedbackByCustomerID("C001");
        if (list.isEmpty()) {
            System.out.println("No feedbacks found for C001.");
        } else {
            for (Feedback f : list) {
                System.out.println(f.getFeedbackID() + " | " + f.getAppointmentID() + " | " + f.getComment() + " | Rating: " + f.getRating());
            }
        }

        System.out.println("\n===== TEST 5: Get feedback for non-existent appointment =====");
        Feedback fb2 = feedbackDAO.getFeedbackByAppointmentID("A999");
        System.out.println("Result: " + (fb2 == null ? "NULL (expected)" : "Found: " + fb2.getFeedbackID()));
    }
}