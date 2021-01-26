package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.FateAction;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;

public class DrainMana
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:DrainMana";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LymeanSkl.png";
    private static final int COST = 1;
    private static final int FATE_AMT = 4;
    private static final int DRAW_AMT = 1;
    private static final int DAZED_AMT = 1;
    private static final int UPGRADE_FORM_FATE_AMT = 2;

    public DrainMana() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        boolean form = isPlayerInThisForm(LymeanGene.ID);
        if (upgraded && form) {
            fate += UPGRADE_FORM_FATE_AMT;
        }
        addToBot(new FateAction(fate));
        if (form || upgraded) {
            addToBot(new DrawCardAction(p, DRAW_AMT));
        }
        addToBot(new MakeTempCardInDrawPileAction(new Dazed(), DAZED_AMT, true, true));
        formEffect(LymeanGene.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new DrainMana();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
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