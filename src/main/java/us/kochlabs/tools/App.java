package us.kochlabs.tools;

import us.kochlabs.tools.life360.Life360Client;
import us.kochlabs.tools.life360.circles.Circles;
import us.kochlabs.tools.life360.circles.Circle;
import us.kochlabs.tools.life360.circles.Member;

public class App 
{
    public static void main(String[] args)
    {
        String username = "";
        String password = "";

        Life360Client client = new Life360Client(username, password);

        if (client.authenticate()) {

            Circles circles = client.getAllCircles();

            for (int i = 0; i < circles.count(); i++) {

                Circle circle = client.getCircleById(circles.getCircleId(i));

                for (int j = 0; j < circle.memberCount; j++) {

                    Member member = circle.getMember(j);
                    System.out.println(member.firstName + " " + member.lastName);

                }
            }

        }
    }
}
