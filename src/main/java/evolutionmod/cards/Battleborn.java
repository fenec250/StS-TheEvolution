package evolutionmod.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.orbs.CentaurGene2;
import evolutionmod.patches.EvolutionEnum;

public class Battleborn
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Battleborn";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurPower.png";
    private static final int COST = 1;
    private static final int POWER_AMT = 2;

    public Battleborn() {
        super(ID, NAME, new RegionName("red/power/inflame"), COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = POWER_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                new BattlebornPower(p, this.magicNumber)));
        if (this.upgraded) {
            addToBot(new CentaurGene2().getChannelAction());
            addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p)));
        } else {
            formEffect(CentaurGene2.ID, () -> addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p))));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Battleborn();
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
        if (!upgraded && isPlayerInThisForm(CentaurGene2.ID)) {
            this.glowColor = CentaurGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }


    public static class BattlebornPower extends TwoAmountPower {
        public static final String POWER_ID = "evolutionmod:BattlebornPower";
        public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = cardStrings.NAME;
        public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

        public BattlebornPower(AbstractCreature owner, int initialAmount) {
            this.name = NAME;
            this.ID = POWER_ID;
            this.owner = owner;
//            this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/AphrodisiacPower84.png"), 0, 0, 84, 84);
//            this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/AphrodisiacPower32.png"), 0, 0, 32, 32);
            this.loadRegion("rushdown");
            this.type = PowerType.BUFF;
            this.amount = initialAmount;
            this.updateDescription();
            this.amount2 = (int)AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().filter(c->c.type == CardType.ATTACK).count();
        }

        @Override
        public void onAfterUseCard(AbstractCard card, UseCardAction action) {
            if (card.type == CardType.ATTACK) {
                this.amount2 += 1;
            }
            super.onAfterUseCard(card, action);
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2];
        }

        public void stackPower(int stackAmount) {
            this.fontScale = 8.0F;
            this.amount += stackAmount;
            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
        }

        public void atStartOfTurn() {
            int vigor = this.amount2 * this.amount;
            addToBot(new ApplyPowerAction(this.owner, this.owner, new VigorPower(this.owner, vigor), vigor));
            this.amount2 = 0;
            this.flash();
        }
    }
}