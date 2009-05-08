package ch.idsia.ai.agents.ai;

import ch.idsia.ai.agents.IAgent;
import ch.idsia.ai.agents.RegisterableAgent;
import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.IEnvironment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 * Package: com.mojang.mario.Agents
 */
public class ForwardAgent extends RegisterableAgent implements IAgent
{
    int trueJumpCounter = 0;
    int trueSpeedCounter = 0;

    public ForwardAgent()
    {
        super("ForwardAgent");
        reset();
    }

    public void reset()
    {
        Action = new boolean[IEnvironment.NumberOfActionSlots];
        Action[Mario.KEY_RIGHT] = true;
        Action[Mario.KEY_SPEED] = true;
        trueJumpCounter = 0;
        trueSpeedCounter = 0;
    }

    private boolean DangerOfGap(byte[][] levelScene)
    {
        for (int x = 9; x < 13; ++x)
        {
            boolean f = true;
            for(int y = 12; y < 22; ++y)
            {
                if  (levelScene[y][x] != 0)
                    f = false;
            }
            if (f && levelScene[12][11] != 0)
                return true;
        }
        return false;
    }

    public boolean[] getAction(IEnvironment observation)
    {
        //TODO: Discuss increasing diffuculty for handling the gaps.
        // this Agent requires observation.

        assert(observation != null);
        byte[][] levelScene = observation.getLevelSceneObservation();

        if (levelScene[11][13] != 0 || levelScene[11][12] != 0 ||  DangerOfGap(levelScene))
        {
            if (observation.mayMarioJump() || ( !observation.isMarioOnGround() && Action[Mario.KEY_JUMP]))
            {
                Action[Mario.KEY_JUMP] = true;
            }
            ++trueJumpCounter;
//            System.out.println("trueJumpCounter:" + trueJumpCounter);
        }
        else
        {
            Action[Mario.KEY_JUMP] = false;
            trueJumpCounter = 0;
        }

        if (trueJumpCounter > 16)
        {
            trueJumpCounter = 0;
            Action[Mario.KEY_JUMP] = false;
        }


//        if (++trueSpeedCounter > 10)
//        {
//            Action[Mario.KEY_SPEED] = false;
//            trueSpeedCounter = 0;
//        }
//        else
//            Action[Mario.KEY_SPEED] = false;

        Action[Mario.KEY_SPEED] = DangerOfGap(levelScene);
        return Action;
    }
}
