package evolutionmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class EvolutionEnum {

    @SpireEnum(name="EVOLUTION_V2")
    public static AbstractPlayer.PlayerClass EVOLUTION_V2_CLASS;
    @SpireEnum
    public static AbstractPlayer.PlayerClass EVOLUTION_CLASS;

    @SpireEnum(name="EVOLUTION_V2_BLUE")
    public static AbstractCard.CardColor EVOLUTION_V2_BLUE;
    @SpireEnum(name="EVOLUTION_BLUE")
    public static AbstractCard.CardColor EVOLUTION_BLUE;

    @SpireEnum(name="EVOLUTION_V2_BLUE")
    public static CardLibrary.LibraryType EVOLUTION_V2_LIBRARY;
    @SpireEnum(name="EVOLUTION_BLUE")
    public static CardLibrary.LibraryType EVOLUTION_LIBRARY;

}