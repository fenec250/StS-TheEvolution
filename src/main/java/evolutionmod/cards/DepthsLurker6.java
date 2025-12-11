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
import evolutionmod.orbs.ShadowGene2;
import evolutionmod.patches.AbstractCardEnum;

public class DepthsLurker6
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:DepthsLurker";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/DepthsLurker.png";
    private static final int COST = 3;
    private static final int BLOCK_AMT = 18;
    private static final int UPGRADE_BLOCK_AMT = 6;

    public DepthsLurker6() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK_AMT;
        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null) {
            this.setCostForTurn(Math.max(COST - GameActionManager.turn/2, 0));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, this.block));
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        this.setCostForTurn(Math.max(COST - GameActionManager.turn/2, 0));
    }

    @Override
    public AbstractCard makeCopy() {
        AbstractCard card = new DepthsLurker6();
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
        }
    }
}