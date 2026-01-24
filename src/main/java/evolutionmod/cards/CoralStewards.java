package evolutionmod.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene2;
import evolutionmod.patches.EvolutionEnum;

public class CoralStewards extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:CoralStewards";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Coral.png";
    private static final int COST = 1;
    private static final int POWER_AMT = 2;
    private static final int UPGRADE_POWER_AMT = 2;
    public static final int BLOCK_THRESHOLD = 8;

    public CoralStewards() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = POWER_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new CoralPower(p, this.magicNumber)));
        addToBot(new InsectGene2().getChannelAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new CoralStewards();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_POWER_AMT);
        }
    }

    public static class CoralPower extends TwoAmountPower {
        public static final String POWER_ID = "evolutionmod:CoralPower";
        public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = cardStrings.NAME;
        public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

        public int fullAmount;
        public AbstractPlayer player;

        public CoralPower(AbstractPlayer owner, int initialAmount) {
            this.name = NAME;
            this.ID = POWER_ID;
            this.owner = owner;
            this.player = owner;
            this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/InsectPower84.png"), 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/InsectPower32.png"), 0, 0, 32, 32);
            this.type = PowerType.BUFF;
            this.amount2 = BLOCK_THRESHOLD;
            this.amount = initialAmount;
            this.fullAmount = initialAmount;
            this.updateDescription();
        }

        public void onGainedBlock(float blockAmount) {
            this.amount2 -= blockAmount;
            while(this.amount2 <= 0 && this.amount > 0) {
                this.amount2 += BLOCK_THRESHOLD;
                this.amount -= 1;
                addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(this.player)));
            }
            if (this.amount == 0 && this.amount2 <1) {
                this.amount2 = 1;
            }
            updateDescription();
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + BLOCK_THRESHOLD + DESCRIPTIONS[1] + fullAmount + DESCRIPTIONS[2]
                    + amount2 + DESCRIPTIONS[3] + amount + DESCRIPTIONS[4];
        }

        @Override
        public void stackPower(int stackAmount) {
            this.fontScale = 8.0F;
            this.amount += stackAmount;
            this.fullAmount = stackAmount;
            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
            else {
                this.updateDescription();
            }
        }

        @Override
        public void atStartOfTurn() {
            this.amount = fullAmount;
            super.atStartOfTurn();
        }
    }
}