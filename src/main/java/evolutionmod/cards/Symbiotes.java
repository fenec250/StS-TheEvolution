package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.GrowthPower;
import evolutionmod.powers.SymbiotesPower;

public class Symbiotes
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Symbiotes";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Symbiotes.png";
    private static final int COST = 1;
    private static final int SYMBIOTES_AMT = 1;
    private static final int FORM_GROWTH_AMT = 1;
    private static final int UPGRADE_GROWTH_AMT = 1;

    public Symbiotes() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = SYMBIOTES_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SymbiotesPower(p, this.magicNumber), this.magicNumber));

//        formEffect(InsectGene.ID, () -> formEffect(PlantGene.ID, () ->
//                addToBot(new RefundAction(this, 1))));
//        formEffect(InsectGene.ID, () -> addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, 1))));

//        if (this.upgraded) {
//            formEffect(PlantGene.ID, () -> addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, FORM_GROWTH_AMT))));
//        }
        int growth = (this.upgraded ? UPGRADE_GROWTH_AMT : 0) + (isPlayerInThisForm(PlantGene.ID) ? FORM_GROWTH_AMT : 0);
        if (growth > 0) {
            addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, growth)));
        }
        formEffect(PlantGene.ID);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
//            this.isInnate = true;
//            this.upgradeMagicNumber(UPGRADE_SYMBIOTES_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(PlantGene.ID)) {
            this.glowColor = PlantGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR;
        }
    }
}