package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import evolutionmod.orbs.MerfolkGene2;
import evolutionmod.patches.EvolutionEnum;

public class DepthsLurker
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:DepthsLurker";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/DepthsLurker.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 4;
    private static final int UPGRADE_BLOCK_AMT = 1;
    private static final int TURN_BLOCK_AMT = 1;
    private static final int UPGRADE_TURN_BLOCK_AMT = 1;
    private static final int FORM_TURN_BLOCK_AMT = 1;

    public DepthsLurker() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = TURN_BLOCK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, this.block));
        formEffect(MerfolkGene2.ID);
    }


    @Override
    protected void applyPowersToBlock() {
        alterBlockAround(() -> super.applyPowersToBlock());
    }

    private void alterBlockAround(Runnable supercall) {
        int mult = this.magicNumber + (isPlayerInThisForm(MerfolkGene2.ID)?FORM_TURN_BLOCK_AMT:0);
        this.baseBlock = BLOCK_AMT + (upgraded ? UPGRADE_BLOCK_AMT : 0) + GameActionManager.turn * mult;
        supercall.run();
        this.baseBlock = BLOCK_AMT + (upgraded ? UPGRADE_BLOCK_AMT : 0);
        this.isBlockModified = this.block != this.baseBlock;
    }

    @Override
    public AbstractCard makeCopy() {
        AbstractCard card = new DepthsLurker();
        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            card.setCostForTurn(Math.max(COST - GameActionManager.turn/2, 0));
        }
        return card;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.upgradeMagicNumber(UPGRADE_TURN_BLOCK_AMT);
        }
    }

    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (isPlayerInThisForm(MerfolkGene2.ID)) {
            this.glowColor = MerfolkGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}