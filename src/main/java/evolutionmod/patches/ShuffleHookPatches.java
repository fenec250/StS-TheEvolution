package evolutionmod.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import evolutionmod.cards.OnShuffleCard;

// Adapted from Vex's:
// https://github.com/DarkVexon/FishingCharacter/blob/3ccf22a7585a6c170bedef356d59c6153f755f61/src/main/java/theFishing/patch/ShuffleHookPatches.java

public class ShuffleHookPatches {
    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class ShufflePatchOne {
        public static void Postfix(EmptyDeckShuffleAction __instance) {
            triggerOnShuffleCards();
        }
    }

    @SpirePatch(clz = ShuffleAction.class, method = "update")
    public static class ShufflePatchTwo {
        public static void Postfix(ShuffleAction __instance) {
            boolean b = ReflectionHacks.getPrivate(__instance, ShuffleAction.class, "triggerRelics");
            if (b) {
                triggerOnShuffleCards();
            }
        }
    }

    @SpirePatch(clz = ShuffleAllAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class ShufflePatchThree {
        public static void Postfix(ShuffleAllAction __instance) {
            triggerOnShuffleCards();
        }
    }

    public static void triggerOnShuffleCards() {
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof OnShuffleCard) {
                ((OnShuffleCard) c).onShuffleFromDrawPile();
            }
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof OnShuffleCard) {
                ((OnShuffleCard) c).onShuffleFromHandPile();
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof OnShuffleCard) {
                ((OnShuffleCard) c).onShuffleFromDiscardPile();
            }
        }
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c instanceof OnShuffleCard) {
                ((OnShuffleCard) c).onShuffleFromExhaustPile();
            }
        }
    }
}