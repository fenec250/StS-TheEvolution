package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import evolutionmod.orbs.InsectGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.FatePower;

public class Hivemind
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Hivemind";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Hivemind.png";
    private static final int COST = 0;
    private static final int FATE_AMT = 2;
    private static final int DRAW_AMT = 1;
    private static final int DAZED_AMT = 1;
    private static final int UPGRADE_FORM_FATE_AMT = 2;

    public Hivemind() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.cardsToPreview = new Drone();
        this.baseMagicNumber = this.magicNumber = FATE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        boolean form = isPlayerInThisForm(InsectGene2.ID);
        if (upgraded && form) {
            fate += UPGRADE_FORM_FATE_AMT;
        }
//        addToBot(new FateAction(fate));
        addToBot(new ApplyPowerAction(p, p, new FatePower(p, fate), fate, true));
        if (form || upgraded) {
            addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, DRAW_AMT), DRAW_AMT, true));
        }
        addToBot(new MakeTempCardInDrawPileAction(new Drone(), DAZED_AMT, true, true));
        formEffect(InsectGene2.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Hivemind();
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
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(InsectGene2.ID)) {
            this.glowColor = InsectGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR;
        }
    }
}