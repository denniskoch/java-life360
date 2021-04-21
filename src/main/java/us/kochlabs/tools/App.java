package us.kochlabs.tools;

import us.kochlabs.tools.life360.Client;
import us.kochlabs.tools.life360.circles.Circles;
import us.kochlabs.tools.life360.circles.Circle;
import us.kochlabs.tools.life360.circles.Member;

public class App 
{
    public static void main( String[] args )
    {
        String username = "";
        String password = "";

        Client client = new Client(username, password);

        if (client.Authenticate()) {

            Circles circles = client.getCircles();

            for (int i = 0; i < circles.count(); i++) {

                Circle circle = client.getCircle(circles.getCircleId(i));

                for (int j = 0; j < circle.memberCount; j++) {

                    Member member = circle.getMember(j);
                    System.out.println(member.firstName + " " + member.lastName);

                }
            }

        }
    }
}
