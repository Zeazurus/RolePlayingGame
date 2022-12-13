import java.util.Random;

public class Battle {
    public static Entity fight(Entity attacker, Entity defender) {
        var lambdaContext = new Object() {
            Entity winner = null;
        };

        Entity[] entities = new Entity[] { attacker, defender };
        //getFightInfo(entities);

        System.out.println("БОЙ НАЧИНАЕТСЯ");
        Runnable runnable = () -> {
            int firstEntity = 0;
            for (int i = 1; ; i++) {
                firstEntity %= 2;
                int secondEntity = (firstEntity + 1) % 2;

                //Замедляет выдачу, чтобы бой не выглядел пулеметной очередью
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("===== Ход " + i + " =====");
                lambdaContext.winner = startHitting(entities[firstEntity], entities[secondEntity]);
                if (lambdaContext.winner != null) {
                    System.out.println("\nБОЙ ОКОНЧЕН!\n");
                    break;
                }

                firstEntity++;
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        /*while(attacker.getCurrentHealth() >= 0 || defender.getCurrentHealth() >= 0)
        {
            //Игрок нападает на Монстра
            System.out.println("Нападаем на Монстра");
            if (doDodge(defender.dexterity)) {
                //Пока не попадем по Монстру, мы не узнаем сколько у него здоровья
                System.out.println("Монстр уклонился!");
            }
            else {
                if (defender.setCurrentHealth(defender.getCurrentHealth() - attacker.strength))
                    System.out.println("У монстра осталось " + defender.getCurrentHealth() + " здоровья!");
                else {
                    winner = attacker;
                    break;
                }
            }

            //Монстр нападает на игрока
            System.out.println("Монстр атакует вас!");
            if (doDodge(attacker.dexterity)) {
                //Пока не попадем по Монстру, мы не узнаем сколько у него здоровья
                System.out.println("Вы уклонились!");
            }
            else {
                if (attacker.setCurrentHealth(attacker.getCurrentHealth() - defender.strength))
                    System.out.println("У вас осталось " + attacker.getCurrentHealth() + " здоровья!");
                else {
                    winner = defender;
                    break;
                }
            }
        }*/

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return lambdaContext.winner;
    }

    private static Entity startHitting(Entity attacker, Entity defender) {
        System.out.println(attacker.name + " нападает на " + defender.name);
        //Уклонение
        if (doDodge(defender.getDexterity())) {
            //Пока не попадем по Монстру, мы не узнаем сколько у него здоровья
            System.out.println(defender.name + " уклонился!");
            return null;
        }
        //Критический удар
        else return hit(attacker, defender, doCriticalStrike());
    }

    private static Entity hit(Entity attacker, Entity defender, boolean isCriticalStrike) {
        //Критический удар
        String hitClassification = "";
        int hitMultiplier = 1;

        if (isCriticalStrike) {
            hitClassification = "КРИТИЧЕСКОГО";
            hitMultiplier = 2;
        }

        //Удар
        int damage = attacker.getStrength() * hitMultiplier;
        System.out.println("И наносит " + hitClassification + " " + damage + " урона!");
        if (defender.setCurrentHealth(defender.getCurrentHealth() - damage)) {
            System.out.println("У " + defender.name + " осталось " + defender.getCurrentHealth() + " здоровья!");
            return null;
        }
        else {
            //Если убил
            return attacker;
        }
    }

    private static boolean doDodge(int dexterity) {
        Random random = new Random();
        return dexterity * 3 > random.nextInt(100);
    }

    private static boolean doCriticalStrike() {
        Random random = new Random();
        return 5 > random.nextInt(100);
    }

    /*private static void getFightInfo(Entity[] entities) {
        System.out.println("ИНФОРМАЦИЯ ПЕРЕД БОЕМ");
        for (Entity entity : entities) {
            getEntityInfo(entity);
        }
    }*/

    /*private static void getEntityInfo(Entity entity) {
        System.out.println(entity.toString());
    }*/
}
