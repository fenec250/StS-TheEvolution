package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.ChannelMagicAction;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;

public class ChannelMagic
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:ChannelMagic";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ChannelMagic.png";
    private static final int COST = 1;
    private static final int FATE_AMT = 2;
    private static final int UPGRADE_FATE_AMT = 1;
    private static final int LYMEAN_FATE_AMT = 1;

    public ChannelMagic() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        LavafolkGene lavafolkGene = new LavafolkGene();
        addToBot(new ChannelAction(lavafolkGene));
        if (isPlayerInThisForm(LymeanGene.ID)) {
            fate += LYMEAN_FATE_AMT;
        }
        addToBot(new ChannelMagicAction(fate, lavafolkGene));
        formEffect(LymeanGene.ID);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FATE_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return 1;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return isPlayerInThisForm(LymeanGene.ID);
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        return LymeanGene.COLOR.cpy();
    }
}