package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;

public class Hatch
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Hatch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/InsectSkl.png";
    private static final int COST = 2;
    private static final int DRONES_AMT = 3;
    private static final int UPGRADE_DRONES_AMT = 1;

    public Hatch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DRONES_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p), this.magicNumber));
        if (this.upgraded || !AbstractGene.isPlayerInThisForm(InsectGene.ID)) {
            addToBot(new ChannelAction(new InsectGene()));
        }
    }

    @Override
    public void applyPowers() {
        calculateDroneAmount();
        super.applyPowers();
        this.isMagicNumberModified = this.magicNumber != DRONES_AMT + (this.upgraded ? UPGRADE_DRONES_AMT : 0);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        calculateDroneAmount();
        super.calculateCardDamage(mo);
        this.isMagicNumberModified = this.magicNumber != DRONES_AMT + (this.upgraded ? UPGRADE_DRONES_AMT : 0);
    }

    private void calculateDroneAmount() {
        this.magicNumber = this.baseMagicNumber = DRONES_AMT;
        if (this.upgraded || AbstractGene.isPlayerInThisForm(InsectGene.ID)) {
            this.magicNumber += UPGRADE_DRONES_AMT;
            this.baseMagicNumber += UPGRADE_DRONES_AMT;
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRONES_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}